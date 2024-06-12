package com.stopsmoke.kekkek.presentation.settings.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.databinding.FragmentSettingsProfileBinding
import com.stopsmoke.kekkek.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File

class SettingsProfileFragment : Fragment() {

    private var _binding: FragmentSettingsProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val viewModel by viewModels<ProfileSettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.circleIvProfile.setImageURI(uri)
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                // 프로필 이미지를 가져오는 부분도 해야함
                if (inputStream != null) {
                    viewModel.settingProfile(inputStream)
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            } else {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        settingProfile.tvUserProfileTitle.text = "계정"
        settingProfile.ivUserProfileBack.setOnClickListener {
            findNavController().popBackStack()
        }
        circleIvProfile.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        when (viewModel.user) {
            is Result.Error -> {}
            Result.Loading -> {}
            is Result.Success -> {
                lifecycleScope.launch {
                    (viewModel.user as Result.Success<Flow<User.Registered>>).data.collect {
                        circleIvProfile.load(it.profileImage) {
                            crossfade(true)
                        }
                    }
                }
            }
        }
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