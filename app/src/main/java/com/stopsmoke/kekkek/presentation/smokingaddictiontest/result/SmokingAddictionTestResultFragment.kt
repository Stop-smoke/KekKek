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
                    tvTestResultType.text = "가장 쉽게 금연할 수 있는 상태"
                    tvTestResultDescription.text =
                        "흡연량과 흡연 시간이 늘어날수록 \n니코틴에 대한 의존도는 높아집니다. \n\n오늘부터 금연 성공을 이어가세요!"
                    ivTestResultIcon.setImageResource(R.drawable.ic_test_good)
                    viewModel.updateCigaretteAddictionTestResult("담배 양호 상태 🙂")
                }

                SmokingQuestionnaireUiState.Medium -> {
                    tvTestResultType.text = "금연을 시작해야 할 상태"
                    tvTestResultDescription.text =
                        "구체적인 증상이 나타나지 않아 \n큰 어려움 없이 금연할 수 있지만, \n\n재흡연도 쉬운 시기입니다."
                    ivTestResultIcon.setImageResource(R.drawable.ic_test_soso)
                    viewModel.updateCigaretteAddictionTestResult("담배 의존 상태 😥")
                }

                SmokingQuestionnaireUiState.High -> {
                    tvTestResultType.text = "니코틴에 대한 의존이 이미 심한 상태"
                    tvTestResultDescription.text =
                        "심한 금단증상으로 \n금연을 이어가기 힘든 경우로 보입니다. \n\n전문가의 도움을 받아보시는 것을 \n추천 드립니다."
                    ivTestResultIcon.setImageResource(R.drawable.ic_thumb_down)
                    viewModel.updateCigaretteAddictionTestResult("담배 중독 상태 😱")
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