package com.stopsmoke.kekkek.presentation.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentTestResultBinding
import com.stopsmoke.kekkek.invisible

class TestResultFragment : Fragment() {

    private var _binding: FragmentTestResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<TestViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTestResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
    }

    private fun setupView() {
        viewModel.getTotalScore()
        observeLiveData()
    }


    private fun observeLiveData() = with(binding) {
        viewModel.testResult.observe(viewLifecycleOwner) { totalScore ->
            when (totalScore) {
                in 8..13 -> {
                    tvTestResultType.text = viewModel.results[0][0].toString()
                    tvTestResultDescription.text = viewModel.results[0][1].toString()
                    ivTestResultIcon.setImageResource(viewModel.results[0][2] as Int)
                    viewModel.updateCigaretteAddictionTestResult("담배 양호 상태 🙂")
                }

                in 14..19 -> {
                    tvTestResultType.text = viewModel.results[1][0].toString()
                    tvTestResultDescription.text = viewModel.results[1][1].toString()
                    ivTestResultIcon.setImageResource(viewModel.results[1][2] as Int)
                    viewModel.updateCigaretteAddictionTestResult("담배 의존 상태 😥")
                }

                else -> {
                    tvTestResultType.text = viewModel.results[2][0].toString()
                    tvTestResultDescription.text = viewModel.results[2][1].toString()
                    ivTestResultIcon.setImageResource(viewModel.results[2][2] as Int)
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

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearScore() // 이 코드를 onDestroyView 에다가 적는게 맞나요?
    }
}