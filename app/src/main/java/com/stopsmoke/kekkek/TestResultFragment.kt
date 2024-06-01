package com.stopsmoke.kekkek

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.stopsmoke.kekkek.databinding.FragmentTestResultBinding

class TestResultFragment : Fragment() {

    var result = 0
    private var _binding: FragmentTestResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTestResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
    }

    private fun setupView() {
        with(binding) {
            if (result in 0..7) {
                tvTestResultType.text = "가장 쉽게 금연할 수 있는 상태"
                tvTestResultDescription.text = "흡연량과 흡연 시간이 늘어날수록 니코틴에 대한 의존도는 높아집니다. 오늘부터 금연 성공을 이어가세요!"
                ivTestResultIcon.setImageResource(R.drawable.ic_test_good)
            } else if (result in 8..16) {
                tvTestResultType.text = "금연을 시작해야 할 상태"
                tvTestResultDescription.text = "구체적인 증상이 나타나지 않아 큰 어려움 없이 금연할 수 있지만, 재흡연도 쉬운 시기입니다."
                ivTestResultIcon.setImageResource(R.drawable.ic_test_soso)
            } else {
                tvTestResultType.text = "니코틴에 대한 의존이 이미 심한 상태"
                tvTestResultDescription.text = "심한 금단증상으로 금연을 이어가기 힘든 경우 전문가의 도움을 받아보시는 것을 추천 드립니다."
                ivTestResultIcon.setImageResource(R.drawable.ic_test_bad)
            }
        }
    }

    private fun setupListener() {
        with(binding) {
            btnTestResultOk.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.main,HomeFragment())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}