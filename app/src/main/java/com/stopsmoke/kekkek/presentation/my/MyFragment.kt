package com.stopsmoke.kekkek.presentation.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.ProfileImage
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.databinding.FragmentMyBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.my.achievement.navigateToAchievementScreen
import com.stopsmoke.kekkek.presentation.my.bookmark.navigateToMyBookmarkScreen
import com.stopsmoke.kekkek.presentation.my.comment.navigateToMyCommentScreen
import com.stopsmoke.kekkek.presentation.my.complaint.navigateToMyComplaintScreen
import com.stopsmoke.kekkek.presentation.my.post.navigateToMyPostScreen
import com.stopsmoke.kekkek.presentation.my.smokingsetting.navigateToSmokingSettingScreen
import com.stopsmoke.kekkek.presentation.my.supportcenter.navigateToSupportCenterScreen
import com.stopsmoke.kekkek.presentation.notification.navigateToNotificationScreen
import com.stopsmoke.kekkek.presentation.settings.navigateToSettingsGraph
import com.stopsmoke.kekkek.presentation.settings.profile.navigateToSettingsProfileScreen
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyFragment : Fragment(), ErrorHandle {
    private var _binding: FragmentMyBinding? = null
    private val binding: FragmentMyBinding get() = _binding!!

    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserActivitiesView()
        initViewModel()
        initButtonListener()
    }

    override fun onResume() {
        super.onResume()
        activity?.visible()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initButtonListener() = with(binding) {
        clMyAchievement.setOnClickListener {
            findNavController().navigateToAchievementScreen()
        }
        includeFragmentMyAppBar.icMyBell.setOnClickListener {
            findNavController().navigateToNotificationScreen()
        }
        includeFragmentMyAppBar.icMySettings.setOnClickListener {
            findNavController().navigateToSettingsGraph()
        }
        clMyMypost.setOnClickListener {
            findNavController().navigateToMyPostScreen()
        }
        clMyMycomment.setOnClickListener {
            findNavController().navigateToMyCommentScreen()
        }
        clMyMybookmarknum.setOnClickListener {
            findNavController().navigateToMyBookmarkScreen()
        }
        clMyAntiSmokingSetting.setOnClickListener {
            findNavController().navigateToSmokingSettingScreen()
        }
        clMyCustomerService.setOnClickListener {
            findNavController().navigateToSupportCenterScreen()
        }
        clMyComplaint.setOnClickListener {
            findNavController().navigateToMyComplaintScreen()
        }
        clMyProfile.setOnClickListener {
            findNavController().navigateToSettingsProfileScreen()
        }
    }

    private fun initUserActivitiesView() = with(binding) {
        viewModel.activities.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is Result.Success -> {
                    tvMyWritingNum.text = it.data.postCount.toString()
                    tvMyCommentNum.text = it.data.commentCount.toString()
                    tvMyBookmarkNum.text = it.data.bookmarkCount.toString()
                }

                is Result.Error -> errorExit(findNavController())
                Result.Loading -> {}
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        user.collectLatestWithLifecycle(lifecycle) { user ->
            if (user != null)
                when (user) {
                    is User.Error -> {
                        errorExit(findNavController())
                    }

                    is User.Guest -> {
                        binding.tvMyName.text = "로그인이 필요합니다."
                        binding.tvMyWritingNum.text = "?"
                        binding.tvMyCommentNum.text = "?"
                        binding.tvMyBookmarkNum.text = "?"
                    }

                    is User.Registered -> {
                        binding.tvMyName.text = user.name
                        binding.tvMyRank.text = "랭킹 ${user.ranking}위"

                        when (user.profileImage) {
                            is ProfileImage.Default -> {
                                binding.ivMyProfile.setImageResource(R.drawable.ic_user_profile_test)
                            }

                            is ProfileImage.Web -> {
                                binding.ivMyProfile.load((user.profileImage as ProfileImage.Web).url) {
                                    crossfade(true)
                                }
                            }
                        }

                        viewModel.setAchievementIdList(user.clearAchievementsList.takeLast(3))
//                    binding.itvMyAchievementNum.text = "${myItem.achievementNum} / 83"
                    }
                }
        }

        currentClearAchievementList.collectLatestWithLifecycle(lifecycle){listResult ->
            when(listResult){
                is Result.Error -> errorExit(findNavController())
                Result.Loading -> {}
                is Result.Success -> {
                    val imageList = listOf(
                        listOf(binding.circleIvMyAchievement1, binding.tvMyAchievement1),
                        listOf(binding.circleIvMyAchievement2, binding.tvMyAchievement2),
                        listOf(binding.circleIvMyAchievement3, binding.tvMyAchievement3)
                    )
                    listResult.data.forEachIndexed { index, achievement ->
                        (imageList[index][0] as ImageView).load(achievement.image)
                        (imageList[index][1] as TextView).text = achievement.name
                    }
                }
            }
        }

        uiState.collectLatestWithLifecycle(lifecycle){uiState ->
            when(uiState){
                MyUiState.ErrorExit -> {
                    errorExit(findNavController())
                }
                is MyUiState.LoggedUiState -> {
                    //위의 crrentClearAchievementList, user 감지 여기 하나로 합치고 싶음.
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_my_toolbar, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle menu item clicks here
        when (item.itemId) {
            R.id.toolbar_my_bell -> {}
            R.id.toolbar_my_setting -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}