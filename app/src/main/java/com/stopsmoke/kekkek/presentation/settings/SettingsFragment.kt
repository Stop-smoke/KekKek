package com.stopsmoke.kekkek.presentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSettingsBinding
import com.stopsmoke.kekkek.presentation.settings.model.MultiViewEnum
import com.stopsmoke.kekkek.presentation.settings.model.OnClickListener
import com.stopsmoke.kekkek.presentation.settings.model.ProfileInfo
import com.stopsmoke.kekkek.presentation.settings.model.SettingsItem

class SettingsFragment : Fragment(), OnClickListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingAdapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        settingAdapter = SettingsAdapter(this@SettingsFragment)
        settingAdapter.itemList = listOf(
            SettingsItem(
                profileInfo = ProfileInfo(
                    profileImg = R.drawable.ic_launcher_background,
                    userNickname = "희진",
                    userDateOfBirth = "2024년 3월 1일",
                    userIntroduction = "글이 길면 이렇게 요약됩니다. 글이 길면 이렇게 요약됩니다. 글이 길면 이렇게 요약됩니다."
                ),
                settingTitle = null,
                version = null,
                cardViewType = MultiViewEnum.MY_PAGE
            ),
            SettingsItem(
                settingTitle = "알림",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "언어",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "테마",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "친구",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "오픈 소스 고지",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "개인 정보 보호 및 보안 안내",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "지원",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                version = "현재 버전 2.0.51",
                cardViewType = MultiViewEnum.VERSION,
                profileInfo = null,
                settingTitle = null
            )
        )
    }

    private fun initView() = with(binding.rvSetting) {
        adapter = settingAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListener() = with(binding) {
        ivSettingBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickProfile(settingItem: SettingsItem) {
        // settingItem 에 정보를 담은 채로 보내야함
        findNavController().navigate("setting_profile")
    }

    override fun onClickSettingList(settingItem: SettingsItem) {
        when(settingItem.settingTitle) {
            "알림" -> {
                findNavController().navigate("setting_notification")
            }
            "언어" -> {
                findNavController().navigate("setting_language")
            }
            "테마" -> {

            }
            "친구" -> {

            }
            "오픈 소스 고지" -> {
                findNavController().navigate("setting_oss")
            }
            "개인 정보 보호 및 보안 안내" -> {
                findNavController().navigate("setting_privatepolicy")
            }
            "지원" -> {
                findNavController().navigate("setting_support")
            }
        }
    }

}