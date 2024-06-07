package com.stopsmoke.kekkek.presentation.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSettingBinding

class SettingFragment : Fragment(), OnClickListener {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingAdapter: SettingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        settingAdapter = SettingAdapter(this@SettingFragment)
        settingAdapter.itemList = listOf(
            SettingItem(
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
            SettingItem(
                settingTitle = "알림",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingItem(
                settingTitle = "언어",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingItem(
                settingTitle = "테마",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingItem(
                settingTitle = "친구",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingItem(
                settingTitle = "오픈 소스 고지",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingItem(
                settingTitle = "개인 정보 보호 및 보안 안내",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingItem(
                settingTitle = "지원",
                cardViewType = MultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingItem(
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

    override fun onClickProfile(settingItem: SettingItem) {
        // settingItem 에 정보를 담은 채로 보내야함
        findNavController().navigate("setting_profile")
    }

    override fun onClickSettingList(settingItem: SettingItem) {
        // 각각의 프래그먼트로 넘어가기
    }

}