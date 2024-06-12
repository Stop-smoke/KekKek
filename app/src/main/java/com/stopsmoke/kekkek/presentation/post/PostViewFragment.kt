package com.stopsmoke.kekkek.presentation.post

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import coil.load
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.MobileAds
import com.stopsmoke.kekkek.databinding.FragmentPostViewBinding
import com.stopsmoke.kekkek.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.getRelativeTime
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.community.CommunityListAdapter
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.visible


class PostViewFragment : Fragment() {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!

    private var post: CommunityWritingItem ?= null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post = arguments?.getParcelable("item",CommunityWritingItem::class.java)
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
        setupListener()
    }

    private fun setupView() = with(binding) {
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        adviewPost.loadAd(adRequest)
        post?.let {
            ivPostPoster.load(it.userInfo.profileImage) {
                crossfade(true)
            }
            tvPostPosterNickname.text = it.userInfo.name
            tvPostPosterRanking.text = "랭킹 ${it.userInfo.rank.toString()}위"
            tvPostHour.text = getRelativeTime(it.postTime)
            tvPostTitle.text = it.postInfo.title
            tvPostDescription.text = it.post
            tvPostHeartNum.text = it.postInfo.like.toString()
            tvPostCommentNum.text = it.postInfo.comment.toString()
            tvPostViewNum.text = it.postInfo.view.toString()
        }
    }

    private fun setupListener() = with(binding) {
        includePostViewAppBar.ivPostBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        btnPostAddComment.setOnClickListener {
            val comment = etPostAddComment.text.toString()

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
}