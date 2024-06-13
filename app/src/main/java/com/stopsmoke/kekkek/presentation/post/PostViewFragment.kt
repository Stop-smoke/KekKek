package com.stopsmoke.kekkek.presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stopsmoke.kekkek.R
import com.google.android.material.snackbar.Snackbar
import com.stopsmoke.kekkek.databinding.FragmentPostViewBinding
import com.stopsmoke.kekkek.databinding.FragmentPostViewBottomsheetDialogBinding
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
            it.userInfo.profileImage.let {imgUrl ->
                if(imgUrl.isNullOrBlank()) ivPostPoster.setImageResource(R.drawable.ic_user_profile_test)
                else ivPostPoster.load(imgUrl)
            }
            tvPostPosterNickname.text = it.userInfo.name
            tvPostPosterRanking.text = "랭킹 ${it.userInfo.rank}위"
            tvPostHour.text = getRelativeTime(it.postTime)
            tvPostTitle.text = it.postInfo.title
            tvPostDescription.text = it.post
            tvPostHeartNum.text = it.postInfo.like.toString()
            tvPostCommentNum.text = it.postInfo.comment.toString()
            tvPostViewNum.text = it.postInfo.view.toString()
            if(it.bookmark == true) includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark_filled)
            else if(it.bookmark == false) includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark)
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
        includePostViewAppBar.ivPostBookmark.setOnClickListener {
            if(post?.bookmark == true) {
                includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark)
                post?.bookmark = false
                Snackbar.make(postView,"해당 게시글을 북마크에서 제거했습니다.", Snackbar.LENGTH_SHORT).show()
                post?.let{ viewModel.deleteBookmarkPost(it)}
            } else {
                includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark_filled)
                post?.bookmark = true
                Snackbar.make(postView,"해당 게시글을 북마크에 추가했습니다.", Snackbar.LENGTH_SHORT).show()
                post?.let { viewModel.addBookmarkPost(it) }
            }
        }
        includePostViewAppBar.ivPostMore.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomsheetDialogBinding = FragmentPostViewBottomsheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomsheetDialogBinding.root)

        bottomsheetDialogBinding.tvEditPost.setOnClickListener {
            bottomSheetDialog.dismiss()
            findNavController().navigate(R.id.action_post_view_to_post_edit)
        }
        bottomsheetDialogBinding.tvDeletePost.setOnClickListener {
            Toast.makeText(requireContext(), "게시글을 삭제하시겠습니까?", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }
        bottomsheetDialogBinding.tvReportPost.setOnClickListener {
            Toast.makeText(requireContext(), "게시물 신고를 작성해주세요.", Toast.LENGTH_SHORT).show()
            findNavController().navigate("my_complaint")
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()

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