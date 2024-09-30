package com.agvber.kekkek.presentation.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.agvber.kekkek.R
import com.agvber.kekkek.common.throttleFirst
import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.databinding.FragmentUserProfileBinding
import com.agvber.kekkek.presentation.collectLatestWithLifecycle
import com.agvber.kekkek.presentation.post.detail.navigateToPostDetailScreen
import com.agvber.kekkek.presentation.userprofile.adapter.UserProfileViewPagerAdapter
import com.agvber.kekkek.presentation.utils.wrapTabIndicatorToTitle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!

    private lateinit var userProfileViewPagerAdapter: UserProfileViewPagerAdapter

    private val viewModel: UserProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("uid")?.let {
            viewModel.updateUid(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserData()
        setupViewpager()
        setupTabLayoutWithViewPager()
        binding.includeUserprofileAppBar.ivUserProfileBack.setOnClickListener {
            findNavController().popBackStack()
        }
        collectPostViewNavigationListener()
    }

    private fun observeUserData() = viewModel.user.collectLatestWithLifecycle(lifecycle) { user ->
        user?.let { user ->
            with(binding.includeUserprofileDetail) {
                if (user.uid.isNotEmpty()) {
                    tvUserProfileName.text = user.name
                    tvUserProfileRanking.text = "랭킹 ${user.ranking}위"
                    tvUserProfileIntroduce.text = user.introduction
                    tvUserProfileFollowerCount.text = "3"
                    tvUserProfileFollowingCount.text = "4"

                    when (user.profileImage) {
                        is ProfileImage.Default -> {
                            ivUserProfileProfileImage.setImageResource(R.drawable.ic_user_profile_test)
                        }

                        is ProfileImage.Web -> {
                            ivUserProfileProfileImage.load((user.profileImage as ProfileImage.Web).url)
                        }
                    }
                } else if (user.uid.isNullOrBlank()) {
                    tvUserProfileName.text = "탈퇴한 유저입니다."
                    tvUserProfileRanking.text = ""
                    tvUserProfileIntroduce.text = "탈퇴한 유저입니다."
                    tvUserProfileFollowerCount.text = "0"
                    tvUserProfileFollowingCount.text = "0"

                    ivUserProfileProfileImage.load(R.drawable.ic_user_profile_test)
                }
            }
        }
    }

    private fun setupViewpager() = with(binding.viewpagerUserprofileFragment) {
        userProfileViewPagerAdapter = UserProfileViewPagerAdapter(this@UserProfileFragment)
        adapter = userProfileViewPagerAdapter
        setUserInputEnabled(false)
    }

    private fun setupTabLayoutWithViewPager() {
        TabLayoutMediator(
            binding.tabUserprofileLayout,
            binding.viewpagerUserprofileFragment
        ) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.user_profile_post)
                1 -> tab.text = getString(R.string.user_profile_comment)
                2 -> tab.text = getString(R.string.user_profile_achievements)
            }
        }.attach()
    }

    private fun collectPostViewNavigationListener() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postDetailScreenNavigate.throttleFirst(300).collectLatest {
                    findNavController().navigateToPostDetailScreen(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        wrapTabIndicatorToTitle(binding.tabUserprofileLayout, 52, 52)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}