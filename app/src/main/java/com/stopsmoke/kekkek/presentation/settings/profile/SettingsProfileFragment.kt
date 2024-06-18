package com.stopsmoke.kekkek.presentation.settings.profile

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.stopsmoke.kekkek.databinding.FragmentSettingsProfileBinding
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsProfileFragment : Fragment() {

    private var _binding: FragmentSettingsProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    viewModel.settingProfile(inputStream)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error resetting input stream",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Error opening input stream", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()

        binding.clProfileLogout.setOnClickListener {
            viewModel.logout()
        }

        binding.clProfileServiceOut.setOnClickListener {
            viewModel.withdraw()
        }

        viewModel.onboardingScreenRequest.collectLatestWithLifecycle(lifecycle) {
            navigateToAuthenticationScreen()
        }
    }

    private fun navigateToAuthenticationScreen() {
        findNavController().navigate(route = "authentication_screen") {
            popUpTo(findNavController().graph.id) {
                inclusive = true
            }
        }
    }

    private fun initView() = with(binding) {
        includeSettingsProfileAppBar.tvSettingsProfileTitle.text = "계정"
        includeSettingsProfileAppBar.ivSettingsProfileBack.setOnClickListener {
            findNavController().popBackStack()
        }
        circleIvProfile.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        initEditNameDialogListener()
        initEditIntroductionDialogListener()
    }

    private fun initEditNameDialogListener() = with(binding) {
        ivSettingEditNickname.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val addDialog = EditNameDialogFragment()
            addDialog.show(fragmentManager, "edit name")
        }

    }

    private fun initEditIntroductionDialogListener() = with(binding) {
        ivSettingEditIntroduction.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val addDialog = EditIntroductionDialogFragment()
            addDialog.show(fragmentManager, "edit introduction")
        }

    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            user.collectLatest { user ->
                onBind(user)
            }
        }
    }

    private fun onBind(user: User) = with(binding) {
        when (user) {
            is User.Error -> {
                Toast.makeText(requireContext(), "Error user profile", Toast.LENGTH_SHORT)
                    .show()
            }

            is User.Guest -> {
                Toast.makeText(requireContext(), "게스트 모드", Toast.LENGTH_SHORT).show()
            }

            is User.Registered -> {
                binding.tvSettingProfileNicknameDetail.text = user.name
                binding.tvSettingProfileIntroductionDetail.text = user.introduction

                when (user.profileImage) {
                    is ProfileImage.Default -> {}
                    is ProfileImage.Web -> {
                        circleIvProfile.load((user.profileImage as ProfileImage.Web).url)
                    }
                }
            }
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        pickMedia.unregister()
    }
}