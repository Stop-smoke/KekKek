package com.stopsmoke.kekkek.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListener() {
        binding.run {
            ivHomeNotification.setOnClickListener {
                // 알림 화면으로 넘어간다.
            }

            ivHomeTimer.setOnClickListener {
                // 타이머가 작동한다. (시간이 흘러간다)
            }

            cvHomePerformance.setOnClickListener {
                // 성과 화면으로 넘어간다.
            }

            ivHomeRanking.setOnClickListener {
                // 랭킹 화면으로 넘어간다.
            }

            ivHomeTest.setOnClickListener {
                // 담배 중독 테스트 화면으로 넘어간다.
            }

            ivHomeRandom.setOnClickListener {
                // 이미지가 바뀐다. → 동기부여 관련된 이미지 API를 받아오거나 이미지랑 그에 맞는 명언 문구 한 20개 정도 파일에 저장한 다음에 랜덤으로 불러오는건 어떨까?
            }
        }
    }
}