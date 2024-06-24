package com.stopsmoke.kekkek.presentation.smokingaddictiontest.assessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.stopsmoke.kekkek.databinding.FragmentSmokingAddictionTestViewpagerBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.smokingaddictiontest.SmokingAddictionTestViewModel

class SmokingAddictionTestViewPagerFragment : Fragment() {

    private var _binding: FragmentSmokingAddictionTestViewpagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<SmokingAddictionTestViewModel>()

    private lateinit var title: String
    private lateinit var question: Array<String>
    private var pageIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(TITLE_PARAMS) ?: "null"
        question = arguments?.getStringArray(QUESTION_PARAMS) ?: emptyArray()
        pageIndex = arguments?.getInt(PAGE_INDEX_PARAMS) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSmokingAddictionTestViewpagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        clickButton()
        collectButtonState()
    }

    private fun setupView() = with(binding) {
        tvQuestionMainText.text = title
        btnQuestionAnswer1.text = question.getOrNull(0)
        btnQuestionAnswer2.text = question.getOrNull(1)
        btnQuestionAnswer3.text = question.getOrNull(2)
    }

    private fun clickButton() = with(binding) {
        listOf(btnQuestionAnswer1, btnQuestionAnswer2, btnQuestionAnswer3).forEach { button ->
            button.setOnClickListener {
                when (button.id) {

                    btnQuestionAnswer1.id -> {
                        viewModel.addScore(pageIndex, 1)
                    }

                    btnQuestionAnswer2.id -> {
                        viewModel.addScore(pageIndex, 2)
                    }

                    btnQuestionAnswer3.id -> {
                        viewModel.addScore(pageIndex, 3)
                    }

                }
                viewModel.updatePageIndex(viewModel.pageIndex.value + 1)
            }
        }
    }

    private fun collectButtonState() =
        viewModel.score.collectLatestWithLifecycle(lifecycle) {
            it.getOrDefault(pageIndex, null)?.let { currentScore ->
                binding.btnQuestionAnswer1.isSelected = false
                binding.btnQuestionAnswer2.isSelected = false
                binding.btnQuestionAnswer3.isSelected = false

                if (currentScore == 1) {
                    binding.btnQuestionAnswer1.isSelected = true
                }
                if (currentScore == 2) {
                    binding.btnQuestionAnswer2.isSelected = true
                }
                if (currentScore == 3) {
                    binding.btnQuestionAnswer3.isSelected = true
                }
            }
        }

    companion object {
        private const val TITLE_PARAMS: String = "title"
        private const val QUESTION_PARAMS: String = "question"
        private const val PAGE_INDEX_PARAMS: String = "page_index"

        @JvmStatic
        fun newInstance(
            title: String,
            question: Array<String>,
            pageIndex: Int,
        ) = SmokingAddictionTestViewPagerFragment().apply {
            arguments = Bundle().apply {
                putString(TITLE_PARAMS, title)
                putStringArray(QUESTION_PARAMS, question)
                putInt(PAGE_INDEX_PARAMS, pageIndex)
            }
        }
    }
}