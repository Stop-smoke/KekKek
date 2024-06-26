package com.stopsmoke.kekkek.presentation.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.databinding.FragmentMyBinding
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding: FragmentMyBinding get() = _binding!!

    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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

    private fun initViewModel() {
        viewModel.userData.collectLatestWithLifecycle(lifecycle) { user ->
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
//                    binding.itvMyAchievementNum.text = "${myItem.achievementNum} / 83"
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