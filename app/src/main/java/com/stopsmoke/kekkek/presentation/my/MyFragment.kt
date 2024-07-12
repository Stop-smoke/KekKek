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

        initListener()
        initViewModel()

        binding.clMyAchievement.setOnClickListener {
            findNavController().navigate("achievement")
        }
        binding.clMyProfile.setOnClickListener {
            findNavController().navigate(R.id.action_my_page_to_setting_profile)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.visible()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initListener() = with(binding) {
        includeFragmentMyAppBar.icMyBell.setOnClickListener {
            findNavController().navigate(R.id.action_my_page_to_notification)
        }
        includeFragmentMyAppBar.icMySettings.setOnClickListener {
            findNavController().navigate(R.id.action_my_page_to_nav_settings)
        }
        clMyMypost.setOnClickListener {
            findNavController().navigate(R.id.action_myPage_to_myWritingList)
        }
        clMyMycomment.setOnClickListener {
            findNavController().navigate(R.id.action_myPage_to_myCommentList)
        }
        clMyMybookmarknum.setOnClickListener {
            findNavController().navigate(R.id.action_myPage_to_myBookmarkList)
        }
        clMyAntiSmokingSetting.setOnClickListener {
            findNavController().navigate(R.id.action_my_page_to_settings_smoking_setting)
        }
        clMyCustomerService.setOnClickListener {
            findNavController().navigate(R.id.action_my_page_to_my_supportcenter)
        }
        clMyComplaint.setOnClickListener {
            findNavController().navigate(R.id.action_my_page_to_my_complaint)
        }
        clMyProfile.setOnClickListener {
            findNavController().navigate(R.id.action_my_page_to_setting_profile)
        }

        viewModel.activities.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is Result.Success -> {
                    binding.tvMyWritingNum.text = it.data.postCount.toString()
                    binding.tvMyCommentNum.text = it.data.commentCount.toString()
                    binding.tvMyBookmarkNum.text = it.data.bookmarkCount.toString()
                }

                else -> {}
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        user.collectLatestWithLifecycle(lifecycle) { user ->
            if (user != null)
                when (user) {
                    is User.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
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

        currentClearAchievementList.collectLatestWithLifecycle(lifecycle){list ->
            val imageList = listOf(
                listOf(binding.circleIvMyAchievement1, binding.tvMyAchievement1),
                listOf(binding.circleIvMyAchievement2, binding.tvMyAchievement2),
                listOf(binding.circleIvMyAchievement3, binding.tvMyAchievement3)
                )
            list.forEachIndexed { index, achievement ->
                (imageList[index][0] as ImageView).load(achievement.image)
                (imageList[index][1] as TextView).text = achievement.name
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