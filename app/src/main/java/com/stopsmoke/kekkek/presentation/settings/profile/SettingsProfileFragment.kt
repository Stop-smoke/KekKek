package com.stopsmoke.kekkek.presentation.settings.profile

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            editDialog(binding.tvSettingProfileNicknameDetail)
        }
        ivSettingEditBirth.setOnClickListener {
            editDialog(binding.tvSettingProfileBirthDetail)
        }
        ivSettingEditIntroduction.setOnClickListener {
            editDialog(binding.tvSettingProfileIntroductionDetail)
        }
    }

    private fun editDialog(textView: TextView) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("편집하기")
        builder.setIcon(R.drawable.ic_post)

        val customView = layoutInflater.inflate(R.layout.profile_edit_custom_dialog,null)
        builder.setView(customView)

        val listener = DialogInterface.OnClickListener { p0, _ ->
            val alert = p0 as AlertDialog
            val edit: EditText? = alert.findViewById(R.id.et_edit)
            textView.text = edit?.text
        }

        builder.setPositiveButton("수정하기", listener)
        builder.setNegativeButton("취소", null)

        builder.show()
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