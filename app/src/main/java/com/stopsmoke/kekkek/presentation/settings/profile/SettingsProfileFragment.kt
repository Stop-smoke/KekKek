package com.stopsmoke.kekkek.presentation.settings.profile

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSettingsProfileBinding
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.collectLatest
import com.stopsmoke.kekkek.presentation.settings.SettingsViewModel

class SettingsProfileFragment : Fragment() {

    private var _binding: FragmentSettingsProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val viewModel: SettingsViewModel by activityViewModels()

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
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
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
        initListener()
    }

    private fun initView() = with(binding) {
        includeSettingsProfileAppBar.tvSettingsProfileTitle.text = "계정"
        includeSettingsProfileAppBar.ivSettingsProfileBack.setOnClickListener {
            findNavController().popBackStack()
        }
        circleIvProfile.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // 온보딩 데이터를 넣어줘야함.
//        tvSettingProfileNicknameDetail.text = viewModel.updateNickname()
//        tvSettingProfileBirthDetail.text = viewModel.updateBirthDate()
//        tvSettingProfileIntroductionDetail.text = viewModel.updateIntroduce()

        viewModel.user.collectLatest(lifecycleScope) {
            when(it) {
                is User.Error -> {
                    Toast.makeText(requireContext(), "Error user profile", Toast.LENGTH_SHORT).show()
                }
                is User.Guest -> {
                    Toast.makeText(requireContext(), "게스트 모드", Toast.LENGTH_SHORT).show()
                }
                is User.Registered -> {
                    when (it.profileImage) {
                        is ProfileImage.Default -> {}
                        is ProfileImage.Web -> {
                            circleIvProfile.load((it.profileImage as ProfileImage.Web).url)
                        }
                    }
                }
            }
        }
    }

    private fun initListener() = with(binding) {
        ivSettingEditNickname.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("닉네임 편집하기")
            builder.setIcon(R.drawable.ic_post)

            val customView = layoutInflater.inflate(R.layout.profile_edit_nickname,null)
            builder.setView(customView)

            builder.setPositiveButton("수정하기", null)
            builder.setNegativeButton("취소", null)

            val dialog = builder.create()
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val edit = customView.findViewById<EditText>(R.id.et_edit_nickname)
                if(edit.text.length > 10) {
                    Toast.makeText(requireContext(),"10글자를 초과하시면 안됩니다!",Toast.LENGTH_SHORT).show()
                }
                else {
                    tvSettingProfileNicknameDetail.text = edit.text
                    dialog.dismiss()
                }
            }
        }

        ivSettingEditIntroduction.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("소개글 편집하기")
            builder.setIcon(R.drawable.ic_post)

            val customView = layoutInflater.inflate(R.layout.profile_edit_introduction,null)
            builder.setView(customView)

            builder.setPositiveButton("수정하기",null)
            builder.setNegativeButton("취소",null)

            val dialog = builder.create()
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val edit = customView.findViewById<EditText>(R.id.et_edit_introduction)
                if(edit.text.length > 50) {
                    Toast.makeText(requireContext(),"글자 수는 50글자 이하로 제한되어 있습니다!",Toast.LENGTH_SHORT).show()
                } else {
                    tvSettingProfileIntroductionDetail.text = edit.text
                    dialog.dismiss()
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