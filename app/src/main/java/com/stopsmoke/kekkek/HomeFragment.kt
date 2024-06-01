package com.stopsmoke.kekkek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
                parentFragmentManager.beginTransaction()
                    .add(R.id.main, TestFragment())
                    .addToBackStack(null)
                    .commit()
            }

            ivHomeRandom.setOnClickListener {
                // 이미지가 바뀐다. → 동기부여 관련된 이미지 API를 받아오거나 이미지랑 그에 맞는 명언 문구 한 20개 정도 파일에 저장한 다음에 랜덤으로 불러오는건 어떨까?
            }
        }
    }
}