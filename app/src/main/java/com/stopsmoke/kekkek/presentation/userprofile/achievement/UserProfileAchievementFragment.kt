package com.stopsmoke.kekkek.presentation.userprofile.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.databinding.FragmentUserProfileAchievementBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.error.errorExit
import com.stopsmoke.kekkek.presentation.my.achievement.AchievementItem
import com.stopsmoke.kekkek.presentation.userprofile.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileAchievementFragment : Fragment() {
    private var _binding: FragmentUserProfileAchievementBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserProfileViewModel by activityViewModels()

    private val listAdapter: UserProfileAchievementListAdapter by lazy{
        UserProfileAchievementListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserProfileAchievementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView(){
        binding.rvUserProfileAchievement.adapter = listAdapter
        binding.rvUserProfileAchievement.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initViewModel() = with(viewModel){
        activities.collectLatestWithLifecycle(lifecycle){
            getCurrentProgress(it)
        }

        achievements.collectLatestWithLifecycle(lifecycle){achievementResult ->
            when(achievementResult){
                is Result.Error -> {
                    achievementResult.exception?.printStackTrace()
                    errorExit(findNavController())
                }
                is Result.Success -> listAdapter.submitList(achievementResult.data)
                Result.Loading -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}