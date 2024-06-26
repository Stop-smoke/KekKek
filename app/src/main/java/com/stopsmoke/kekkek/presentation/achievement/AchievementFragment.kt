package com.stopsmoke.kekkek.presentation.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.databinding.FragmentAchievementBinding
import com.stopsmoke.kekkek.domain.model.Achievement
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.firestore.model.AchievementEntity
import com.stopsmoke.kekkek.presentation.achievement.adapter.AchievementListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AchievementFragment : Fragment() {

    private var _binding: FragmentAchievementBinding? = null
    private val binding: FragmentAchievementBinding get() = _binding!!

    private val achievementListAdapter: AchievementListAdapter by lazy {
        AchievementListAdapter(viewModel)
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
        binding.icludeAchievementTop.tvAchievementQuitSmokingDayCount.text = viewModel.getCurrentItem().time.toString()
    }


    private fun initView() = with(binding){
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
        viewLifecycleOwner.lifecycleScope.launch {
            achievements.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { achievements ->
                    achievementListAdapter.submitData(achievements)
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

internal fun AchievementEntity.asExternalModel() = Achievement(
    id = id ?: "null",
    name = name ?: "null",
    description = description ?: "null",
    image = image ?: "null",
    category = when (category) {
        "comment" -> DatabaseCategory.COMMENT
        "post" -> DatabaseCategory.POST
        "user" -> DatabaseCategory.USER
        "achievement" -> DatabaseCategory.ACHIEVEMENT
        "rank" -> DatabaseCategory.RANK
        "all" -> DatabaseCategory.ALL
        else -> DatabaseCategory.ALL
    },
    maxProgress = maxProgress ?: 0,
    requestCode = requestCode ?: "null"
)

internal fun Achievement.getItem() = AchievementItem(
    id = id,
    name = name,
    description = description,
    image = image,
    category = category,
    maxProgress = maxProgress,
    requestCode = requestCode
)