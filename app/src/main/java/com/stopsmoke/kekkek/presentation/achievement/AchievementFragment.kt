package com.stopsmoke.kekkek.presentation.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.databinding.FragmentAchievementBinding
import com.stopsmoke.kekkek.presentation.achievement.adapter.AchievementListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AchievementFragment : Fragment() {

    private var _binding: FragmentAchievementBinding? = null
    private val binding: FragmentAchievementBinding get() = _binding!!

    private lateinit var achievementListAdapter: AchievementListAdapter

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
        setupAppBar()
        setupRecyclerView()
        observeAchievementsItems()
        binding.icludeAchievementTop.tvAchievementQuitSmokingDayCount.text = "183 일"
    }

    private fun setupRecyclerView() = with(binding.rvAchievementItem) {
        achievementListAdapter = AchievementListAdapter(viewModel)
        adapter = achievementListAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAppBar() = with(binding.includeAchievementAppBar) {
        ivAchievementBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeAchievementsItems() = lifecycleScope.launch {
        viewModel.achievements.collectLatest {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}