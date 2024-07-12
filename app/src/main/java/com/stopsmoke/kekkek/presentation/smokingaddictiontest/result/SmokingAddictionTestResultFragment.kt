package com.stopsmoke.kekkek.presentation.smokingaddictiontest.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingAddictionTestResultBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.smokingaddictiontest.SmokingAddictionTestViewModel
import com.stopsmoke.kekkek.presentation.smokingaddictiontest.model.SmokingQuestionnaireUiState

class SmokingAddictionTestResultFragment : Fragment() {

    private var _binding: FragmentSmokingAddictionTestResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<SmokingAddictionTestViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSmokingAddictionTestResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectSmokingResultStateView()
        setupListener()
    }

    private fun collectSmokingResultStateView() = with(binding) {
        viewModel.result.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                SmokingQuestionnaireUiState.Low -> {
                    tvTestResultType.text = getString(R.string.test_good_title)
                    tvTestResultDescription.text = getString(R.string.test_good_content)
                    ivTestResultIcon.setImageResource(R.drawable.ic_test_good)
                    viewModel.updateCigaretteAddictionTestResult(getString(R.string.test_good_home))
                }

                SmokingQuestionnaireUiState.Medium -> {
                    tvTestResultType.text = getString(R.string.test_soso_title)
                    tvTestResultDescription.text = getString(R.string.test_soso_content)
                    ivTestResultIcon.setImageResource(R.drawable.ic_test_soso)
                    viewModel.updateCigaretteAddictionTestResult(getString(R.string.test_soso_home))
                }

                SmokingQuestionnaireUiState.High -> {
                    tvTestResultType.text = getString(R.string.test_bad_title)
                    tvTestResultDescription.text = getString(R.string.test_bad_content)
                    ivTestResultIcon.setImageResource(R.drawable.ic_test_bad)
                    viewModel.updateCigaretteAddictionTestResult(getString(R.string.test_bad_home))
                }
            }
        }
    }

    private fun setupListener() {
        with(binding) {
            btnTestResultOk.setOnClickListener {
                findNavController().popBackStack("home", false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clear()
        _binding = null
    }
}