package com.stopsmoke.kekkek.presentation.userprofile.achievement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentUserProfileAchievementBinding
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.presentation.my.achievement.AchievementItem
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
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

        achievements.collectLatestWithLifecycle(lifecycle){
            listAdapter.submitList(sortedAchievement(it))
        }
    }

    private fun sortedAchievement(list: List<AchievementItem>): List<AchievementItem>{
        val clearList = list.filter { it.progress >= 1.0.toBigDecimal() }
        val nonClearList = list.filter { it !in clearList }.sortedByDescending { it.progress }

        return nonClearList + clearList
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}