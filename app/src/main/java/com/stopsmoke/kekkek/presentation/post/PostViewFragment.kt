package com.stopsmoke.kekkek.presentation.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
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
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setupView()
//        setupListener()
    }

    private fun setupView() = with(binding) {
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
//        binding.run {
//            adviewPost.loadAd(adRequest)
//        }
//        tvPostTitle.text = postTitle
    }

//    private fun setupListener() = with(binding) {
//        ivPostBack.setOnClickListener {
//            requireActivity().supportFragmentManager.popBackStack()
//        }
//        btnPostViewCommentRegister.setOnClickListener {
//            val comment = etPostAddComment.text.toString()
//        }
//    }

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
        fun newInstance(title: String) =
            PostViewFragment().apply {
                arguments = Bundle().apply {
                    putString("POST_LIST", title)
                }
            }
    }
}