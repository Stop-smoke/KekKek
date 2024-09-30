package com.agvber.kekkek.presentation.settings.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.databinding.FragmentSettingsProfileBinding
import com.agvber.kekkek.presentation.authentication.navigateToAuthenticationScreenWithBackStackClear
import com.agvber.kekkek.presentation.collectLatestWithLifecycle
import com.agvber.kekkek.presentation.dialog.CircularProgressDialogFragment
import com.agvber.kekkek.presentation.settings.SettingsViewModel
import com.agvber.kekkek.presentation.settings.model.ProfileImageUploadUiState
import com.agvber.kekkek.presentation.settings.profile.model.ExitAppUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

@AndroidEntryPoint
class SettingsProfileFragment : Fragment() {

    private var _binding: FragmentSettingsProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val viewModel: SettingsViewModel by activityViewModels()

    private val progressDialog = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        CircularProgressDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                handleImageUrl(uri)
            } else {
                Toast.makeText(requireContext(), "Error opening input stream", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun handleImageUrl(uri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val rotatedBitmap = getCorrectlyOrientedBitmap(uri, bitmap)
            val rotatedInputStream = bitmapToInputStream(rotatedBitmap)
            if (rotatedInputStream != null) {
                viewModel.settingProfile(rotatedInputStream)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error converting bitmap to inputstream",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Error resetting input stream",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun bitmapToInputStream(bitmap: Bitmap): InputStream? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        return if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)) {
            ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        } else null
    }

    private fun getCorrectlyOrientedBitmap(uri: Uri, bitmap: Bitmap): Bitmap {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val exif = inputStream?.let { ExifInterface(it) }
        val orientation =
            exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        inputStream?.close()

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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
            val dialog = SettingServiceOutDialogFragment()
            dialog.show(childFragmentManager, "ServiceOutDialogFragment")
        }

        viewModel.exitAppUiState
            .distinctUntilChanged()
            .collectLatestWithLifecycle(lifecycle, Lifecycle.State.CREATED) {
                when (it) {
                    is ExitAppUiState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.settings_user_profile_with_draw_fail_message),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is ExitAppUiState.Logout -> {
                        findNavController().navigateToAuthenticationScreenWithBackStackClear()
                    }

                    is ExitAppUiState.Withdraw -> {
                        findNavController().navigateToAuthenticationScreenWithBackStackClear()
                    }
                }
            }

        viewModel.profileImageUploadUiState.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is ProfileImageUploadUiState.Error -> {
                    progressDialog.value.dismiss()
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    it.t?.printStackTrace()
                    viewModel.initProfileImageUploadUiState()
                }

                is ProfileImageUploadUiState.Init -> {}
                is ProfileImageUploadUiState.Progress -> {
                    showProgressDialog()
                }

                is ProfileImageUploadUiState.Success -> {
                    progressDialog.value.dismiss()
                    Toast.makeText(requireContext(), "업로드 완료!", Toast.LENGTH_SHORT).show()
                    viewModel.initProfileImageUploadUiState()
                }
            }
        }
    }

    private fun showProgressDialog() {
        progressDialog.value.show(childFragmentManager, CircularProgressDialogFragment.TAG)
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
            val addDialog =
                EditNameDialogFragment((viewModel.user.value as? User.Registered)?.name ?: "")
            addDialog.show(fragmentManager, "edit name")
        }

    }

    private fun initEditIntroductionDialogListener() = with(binding) {
        ivSettingEditIntroduction.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val addDialog = EditIntroductionDialogFragment(
                (viewModel.user.value as? User.Registered)?.introduction ?: ""
            )
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

    private fun onBind(user: User?) = with(binding) {
        if (user != null)
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

    override fun onDestroyView() {
        super.onDestroyView()
        if (progressDialog.isInitialized()) {
            progressDialog.value.dismiss()
        }
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        pickMedia.unregister()
    }
}