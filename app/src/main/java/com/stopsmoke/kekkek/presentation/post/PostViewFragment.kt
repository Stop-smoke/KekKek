package com.stopsmoke.kekkek.presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostViewBinding
import com.stopsmoke.kekkek.domain.model.CommentPostData
import com.stopsmoke.kekkek.getRelativeTime
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.getParcelableAndroidVersionSupport
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostViewFragment : Fragment() {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!

    private var post: CommunityWritingItem? = null

    private val viewModel: PostViewModel by viewModels()

    private lateinit var postCommentAdapter: PostCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post = arguments?.getParcelableAndroidVersionSupport(
            key = "item",
            clazz = CommunityWritingItem::class.java
        )
        post?.postInfo?.let { viewModel.updatePostId(it.id) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPostViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()

        postCommentAdapter = PostCommentAdapter()
        binding.rvPostComment.adapter = postCommentAdapter
        binding.rvPostComment.layoutManager = LinearLayoutManager(requireContext())

        viewModel.comment.collectLatestWithLifecycle(lifecycle) {
            postCommentAdapter.submitData(it)
        }

        viewModel.commentCount.collectLatestWithLifecycle(lifecycle) {
            binding.tvPostCommentNum.text = it.toString()
        }

        binding.ivPostPoster.setOnClickListener {
            findNavController().navigate(
                R.id.action_post_view_to_user_profile, bundleOf(
                    "uid" to (post?.userInfo?.uid ?: "")
                )
            )
        }

        viewModel.post.collectLatestWithLifecycle(lifecycle) {
            with(binding) {
                tvPostHeartNum.text = it.firstOrNull()?.likeUser?.size.toString()
                tvPostViewNum.text = it.firstOrNull()?.views.toString()
            }
        }

        postCommentAdapter.registerCallback(
            object : PostCommentCallback {
                override fun deleteItem(commentId: String) {
                    viewModel.deleteComment(commentId)
                    postCommentAdapter.refresh()
                }
            }
        )

        binding.clPostViewHeart.setOnClickListener {
            viewModel.toggleLikeToPost()
        }

    }

    private fun setupView() = with(binding) {
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        adviewPost.loadAd(adRequest)
        post?.let {
            ivPostPoster.load(it.userInfo.profileImage)
            tvPostPosterNickname.text = it.userInfo.name
            tvPostPosterRanking.text = "랭킹 ${it.userInfo.rank}위"
            tvPostHour.text = getRelativeTime(it.postTime)
            tvPostTitle.text = it.postInfo.title
            tvPostDescription.text = it.post
            tvPostHeartNum.text = it.postInfo.like.toString()
            tvPostViewNum.text = it.postInfo.view.toString()
        }
    }

    private fun setupListener() = with(binding) {
        includePostViewAppBar.ivPostBack.setOnClickListener {
            findNavController().popBackStack()
        }
        btnPostAddComment.setOnClickListener {
            if (post == null) {
                return@setOnClickListener
            }
            val comment = etPostAddComment.text.toString()

            viewModel.addComment(
                commentPostData = CommentPostData(
                    post!!.postType,
                    postId = post!!.postInfo.id,
                    postTitle = post!!.postInfo.title
                ),
                text = comment
            )
            postCommentAdapter.refresh()
            binding.etPostAddComment.setText("")
            binding.root.hideSoftKeyboard()
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

    override fun onDestroy() {
        super.onDestroy()
        postCommentAdapter.unregisterCallback()
    }

    private fun View.hideSoftKeyboard() {
        val inputMethodManager = getSystemService(context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
    }
}