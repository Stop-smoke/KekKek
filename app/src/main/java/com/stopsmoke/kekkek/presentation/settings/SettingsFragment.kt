package com.stopsmoke.kekkek.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.stopsmoke.kekkek.BuildConfig
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSettingsBinding
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.settings.model.SettingsItem
import com.stopsmoke.kekkek.presentation.settings.model.SettingsMultiViewEnum
import com.stopsmoke.kekkek.presentation.settings.model.SettingsOnClickListener

class SettingsFragment : Fragment(), SettingsOnClickListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingAdapter: SettingsAdapter

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        observeUserInformation()
    }

    override fun onResume() {
        super.onResume()
        val isNotificationActive =
            NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
        Log.d("active",isNotificationActive.toString())
        if(isNotificationActive) {
            binding.tvSettingNotificationActive.text = "켜짐"
        } else {
            binding.tvSettingNotificationActive.text = "꺼짐"
        }
        activity?.invisible()
    }

    private fun observeUserInformation() {
        viewModel.user.collectLatestWithLifecycle(lifecycle) {
            if (it != null)
                when (it) {
                    is User.Error -> {
                        Toast.makeText(requireContext(), "Error user profile", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is User.Guest -> {
                        binding.includeSettingsProfile.tvSettingUsername.text = "로그인이 필요합니다."
                    }

                    is User.Registered -> {
                        binding.includeSettingsProfile.tvSettingUsername.text = it.name
                        when (it.profileImage) {
                            ProfileImage.Default -> {
                                binding.includeSettingsProfile.circleIvSettingProfile
                                    .setImageResource(R.drawable.ic_user_profile_test)
                            }

                            is ProfileImage.Web -> {
                                binding.includeSettingsProfile.circleIvSettingProfile
                                    .load((it.profileImage as? ProfileImage.Web)?.url)
                            }
                        }
                    }
                }
        }
    }

    private fun initData() {
        settingAdapter = SettingsAdapter(this@SettingsFragment)
        settingAdapter.itemList = listOf(
//            SettingsItem(
//                profileInfo = ProfileInfo(
//                    profileImg = R.drawable.ic_launcher_background,
//                    userNickname = "희진",
//                    userDateOfBirth = "2024년 3월 1일",
//                    userIntroduction = "글이 길면 이렇게 요약됩니다. 글이 길면 이렇게 요약됩니다. 글이 길면 이렇게 요약됩니다."
//                ),
//                settingTitle = null,
//                version = null,
//                cardViewType = SettingsMultiViewEnum.MY_PAGE
//            ),
//            SettingsItem(
//                settingTitle = "알림",
//                cardViewType = SettingsMultiViewEnum.LIST,
//                profileInfo = null,
//                version = null
//            ),
//            SettingsItem(
//                settingTitle = "언어",
//                cardViewType = SettingsMultiViewEnum.LIST,
//                profileInfo = null,
//                version = null
//            ),
//            SettingsItem(
//                settingTitle = "테마",
//                cardViewType = SettingsMultiViewEnum.LIST,
//                profileInfo = null,
//                version = null
//            ),
//            SettingsItem(
//                settingTitle = "친구",
//                cardViewType = SettingsMultiViewEnum.LIST,
//                profileInfo = null,
//                version = null
//            ),
            SettingsItem(
                settingTitle = "오픈 소스 고지",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "개인 정보 보호 및 보안 안내",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "지원",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                version = "현재 버전 ${BuildConfig.VERSION_NAME}",
                cardViewType = SettingsMultiViewEnum.VERSION,
                profileInfo = null,
                settingTitle = null
            )
        )
    }

    private fun initView() = with(binding) {
        rvSetting.adapter = settingAdapter
        rvSetting.layoutManager = LinearLayoutManager(requireContext())
        includeSettingsAppBar.ivSupportCenterBack.setOnClickListener {
            findNavController().popBackStack()
        }
        includeSettingsProfile.root.setOnClickListener {
            findNavController().navigate(R.id.action_setting_to_setting_profile)
        }
        settingNotification.setOnClickListener {
            notification()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickProfile(settingItem: SettingsItem) {
        // settingItem 에 정보를 담은 채로 보내야함
        findNavController().navigate(R.id.action_setting_to_setting_profile)
    }

    private fun notification() {
        val intent = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                Intent().apply {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, activity?.packageName)
                }
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                Intent().apply {
                    action = "android.settings.APP_NOTIFICAITON_SETTINGS"
                    putExtra("app_package", activity?.packageName)
                    putExtra("app_uid", activity?.applicationInfo?.uid)
                }
            }

            else -> {
                Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    addCategory(Intent.CATEGORY_DEFAULT)
                    data = Uri.parse("package:${activity?.packageName}")
                }
            }
        }
        startActivity(intent)
    }

    override fun onClickSettingList(settingItem: SettingsItem) {
        when (settingItem.settingTitle) {
            "언어" -> {
                findNavController().navigate(R.id.action_setting_to_setting_language)
            }

            "테마" -> {

            }

            "친구" -> {

            }

            "오픈 소스 고지" -> {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle(getString(com.stopsmoke.kekkek.R.string.custom_license_title))
            }

            "개인 정보 보호 및 보안 안내" -> {
                findNavController().navigate(R.id.action_setting_to_setting_privatepolicy)
            }

            "지원" -> {
                findNavController().navigate(R.id.action_setting_to_setting_support)
            }
        }
    }
}