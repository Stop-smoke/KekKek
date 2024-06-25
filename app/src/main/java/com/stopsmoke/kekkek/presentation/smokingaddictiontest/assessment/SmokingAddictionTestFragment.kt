package com.stopsmoke.kekkek.presentation.smokingaddictiontest.assessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingAddictionTestBinding
import com.stopsmoke.kekkek.presentation.smokingaddictiontest.SmokingAddictionTestViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SmokingAddictionTestFragment : Fragment() {

    private var _binding: FragmentSmokingAddictionTestBinding? = null
    private val binding get() = _binding!!

    private lateinit var smokingAddictionTestAdapter: SmokingAddictionTestAdapter

    private val viewModel by activityViewModels<SmokingAddictionTestViewModel>()

    private val exitDialog = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        SmokingAddictionTestExitDialogFragment()
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.viewpagerTest.currentItem == 0) {
                findNavController().popBackStack()
                return
            }

            viewModel.updatePageIndex(viewModel.pageIndex.value - 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSmokingAddictionTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

        binding.includeQuestionAppBar.ivTestBack.setOnClickListener {
            backPressedCallback.handleOnBackPressed()
        }
        binding.includeQuestionAppBar.ivTestCancel.setOnClickListener {
            exitDialog.value.show(childFragmentManager, "ExitDialog")
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pageIndex.collectLatest {
                    binding.progressbarQuestion.setProgress(it, true)

                    if (smokingAddictionTestAdapter.itemCount - 1 == binding.viewpagerTest.currentItem) {
                        findNavController().navigate(R.id.action_smoking_questionnaire_question_screen_to_smoking_questionnaire_result_screen)
                        this@launch.cancel()
                    }
                    binding.viewpagerTest.setCurrentItem(it, false)
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(backPressedCallback)
    }

    private fun setupView() {
        smokingAddictionTestAdapter = SmokingAddictionTestAdapter(this)
        binding.viewpagerTest.adapter = smokingAddictionTestAdapter
        binding.viewpagerTest.isUserInputEnabled = false // 사용자가 페이지를 넘기지 못하도록 만듬
        binding.progressbarQuestion.max = smokingAddictionTestAdapter.itemCount
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
        _binding = null
    }
}

