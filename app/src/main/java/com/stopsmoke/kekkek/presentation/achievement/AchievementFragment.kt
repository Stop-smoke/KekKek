package com.stopsmoke.kekkek.presentation.achievement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.databinding.FragmentAchievementBinding
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.achievement.adapter.AchievementListAdapter
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : Fragment() {

    private var _binding: FragmentAchievementBinding? = null
    private val binding: FragmentAchievementBinding get() = _binding!!

    private val achievementListAdapter: AchievementListAdapter by lazy {
        AchievementListAdapter()
    }

    private val viewModel: AchievementViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }


    private fun initView() = with(binding) {
        setupAppBar()
        setupRecyclerView()
    }

    private fun setupRecyclerView() = with(binding.rvAchievementItem) {
        adapter = achievementListAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAppBar() = with(binding.includeAchievementAppBar) {
        ivAchievementBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewModel() = with(viewModel) {
        activities.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is Result.Success -> {
                    viewModel.getCurrentProgress()
                }
                else -> {}
            }
        }

        achievements.collectLatestWithLifecycle(lifecycle) {
            achievementListAdapter.submitList(sortedAchievement(it))
        }

        user.collectLatestWithLifecycle(lifecycle){
            bindTopProgress()
        }
    }

    private suspend fun bindTopProgress() = with(binding) {
        val progress = viewModel.getCurrentItem()
        val user = viewModel.user.value as User.Registered
        val maxProgressCount = viewModel.getAchievementCount()
        icludeAchievementTop.tvAchievementQuitSmokingDayCount.text = "${progress.user} 일"
        icludeAchievementTop.tvAchievementQuitSmokingCount.text =
            "${user.clearAchievementsList.size} / ${maxProgressCount}"
    }

    private fun sortedAchievement(list: List<AchievementItem>): List<AchievementItem>{
        val clearList = list.filter { it.progress >= 1.0.toBigDecimal() }
        val nonClearList = list.filter { it !in clearList }.sortedByDescending { it.progress }

        val insertClearList = clearList.filter { it.id !in (viewModel.user.value as User.Registered).clearAchievementsList }
        if(insertClearList.isNotEmpty()) {
            viewModel.upDateUserAchievementList(insertClearList.map { it.id })
        }
        return nonClearList + clearList
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.visible()
    }
}

