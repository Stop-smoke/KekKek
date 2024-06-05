package com.stopsmoke.kekkek.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.MobileAds
import com.stopsmoke.kekkek.databinding.FragmentPostViewBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.visible


class PostViewFragment : Fragment() {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!

    private var postTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postTitle = it.getString("POST_LIST")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
    }

    private fun setupView() = with(binding) {
//        MobileAds.initialize(requireContext()) // requireActivity 넣어도 오류가 안나는데.. requireContext 랑 requireActivity의 차이가 뭐지?
//        val adRequest = AdRequest.Builder().build()
//        binding.run {
//            // 시간이 된다면 run 과 with 의 차이를 확실히 이해해보자!
//            adviewPost.loadAd(adRequest)
//        }
        tvPostTitle.text = postTitle
    }

    private fun setupListener() = with(binding) {
        ivPostBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.visible()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String) = // 이 부분은 나중에 data class 로 바꿔야 할 것 같습니다.
            PostViewFragment().apply {
                arguments = Bundle().apply {
                    putString("POST_LIST", title)
                }
            }
    }
}