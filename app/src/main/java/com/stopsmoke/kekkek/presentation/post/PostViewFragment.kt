package com.stopsmoke.kekkek.presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostViewBinding
import com.stopsmoke.kekkek.databinding.FragmentPostViewBottomsheetDialogBinding
import com.stopsmoke.kekkek.domain.model.CommentPostData
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.getRelativeTime
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.community.CommunityViewModel
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.getParcelableAndroidVersionSupport
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull


@AndroidEntryPoint
class PostViewFragment : Fragment() {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!

    private var post: CommunityWritingItem? = null

    private val viewModel: PostViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by activityViewModels()

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

        viewModel.post.collectLatestWithLifecycle(lifecycle) {
            val user = viewModel.user.firstOrNull() as? User.Registered ?: return@collectLatestWithLifecycle

            if (it.firstOrNull()?.bookmarkUser?.contains(user.uid) == true) {
                binding.includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark_filled)
                return@collectLatestWithLifecycle
            }

            binding.includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark)
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
//            if(it.bookmark == true) includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark_filled)
//            else if(it.bookmark == false) includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark)
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
            viewModel.toggleBookmark()

//            if(post?.bookmark == true) {
//                includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark)
//                post?.bookmark = false
//                Snackbar.make(postView,"해당 게시글을 북마크에서 제거했습니다.", Snackbar.LENGTH_SHORT).show()
//                post?.let{ viewModel.deleteBookmarkPost(it)}
//            } else {
//                includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark_filled)
//                post?.bookmark = true
//                Snackbar.make(postView,"해당 게시글을 북마크에 추가했습니다.", Snackbar.LENGTH_SHORT).show()
//                post?.let { viewModel.addBookmarkPost(it) }
//            }
        }
        includePostViewAppBar.ivPostMore.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomsheetDialogBinding = FragmentPostViewBottomsheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomsheetDialogBinding.root)

        // 일단
        viewModel.user.collectLatestWithLifecycle(lifecycle) { user ->
            when(user){
                is User.Error -> TODO()
                User.Guest -> TODO()
                is User.Registered ->
                if (post?.userInfo?.uid == user.uid) {
                    bottomsheetDialogBinding.tvDeletePost.visibility = View.VISIBLE
                    bottomsheetDialogBinding.tvDeletePost.setOnClickListener {
                        showDeleteConfirmationDialog()
                        bottomSheetDialog.dismiss()
                    }
                } else {
                    bottomsheetDialogBinding.tvDeletePost.visibility = View.GONE
                }
            }
        }

        bottomsheetDialogBinding.tvEditPost.setOnClickListener {
            bottomSheetDialog.dismiss()
            findNavController().navigate(R.id.action_post_view_to_post_edit)
        }

        bottomsheetDialogBinding.tvReportPost.setOnClickListener {
            Toast.makeText(requireContext(), "게시물 신고를 작성해주세요.", Toast.LENGTH_SHORT).show()
            findNavController().navigate("my_complaint")
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()

    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("게시글 삭제")
            .setMessage("게시글을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { dialog, _ ->
                post?.postInfo?.id?.let { postId ->
                    viewModel.deletePost(postId)
                    Toast.makeText(requireContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    communityViewModel.setPostDeleted(true)
                    findNavController().popBackStack()
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()    }

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