package com.stopsmoke.kekkek.presentation.post.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.databinding.FragmentPostDetailBinding
import com.stopsmoke.kekkek.databinding.FragmentPostViewBottomsheetDialogBinding
import com.stopsmoke.kekkek.presentation.NavigationKey
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.dialog.CommonDialogFragment
import com.stopsmoke.kekkek.presentation.dialog.CommonDialogListener
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.hideSoftKeyboard
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.my.complaint.navigateToMyComplaintScreen
import com.stopsmoke.kekkek.presentation.post.detail.adapter.PostDetailAdapter
import com.stopsmoke.kekkek.presentation.post.detail.adapter.PreviewCommentAdapter
import com.stopsmoke.kekkek.presentation.post.detail.callback.PostCommentCallback
import com.stopsmoke.kekkek.presentation.post.detail.model.PostContentItem
import com.stopsmoke.kekkek.presentation.post.edit.navigateToPostEditScreen
import com.stopsmoke.kekkek.presentation.putNavigationResult
import com.stopsmoke.kekkek.presentation.reply.navigateToReplyScreen
import com.stopsmoke.kekkek.presentation.userprofile.navigateToUserProfileScreen
import com.stopsmoke.kekkek.presentation.utils.CustomItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment : Fragment(), PostCommentCallback, ErrorHandle {

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostDetailViewModel by viewModels()

    private lateinit var postDetailAdapter: PostDetailAdapter
    private lateinit var previewCommentAdapter: PreviewCommentAdapter
    private lateinit var postConcatAdapter: ConcatAdapter

    private lateinit var commentDeleteId: String
    private lateinit var replyDeleteId: String

    private val postDeleteDialog = lazy {
        CommonDialogFragment.newInstance(
            title = getString(R.string.post_detail_post_delete_title),
            description = getString(R.string.post_detail_post_delete_description),
            positiveText = getString(R.string.post_detail_post_delete_positive),
            negativeText = getString(R.string.post_detail_post_delete_negative)
        )
            .apply {
                val commentDeleteDialogListener = object : CommonDialogListener {
                    override fun onPositive() {
                        viewModel.deletePost()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.post_detail_post_delete_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().putNavigationResult(NavigationKey.IS_DELETED_POST, true)
                        findNavController().popBackStack()
                        dismiss()
                    }

                    override fun onNegative() {
                        dismiss()
                    }
                }
                registerCallbackListener(commentDeleteDialogListener)
            }
    }

    private val commentDeleteDialog = lazy {
        CommonDialogFragment.newInstance(
            title = getString(R.string.post_detail_comment_delete_title),
            description = getString(R.string.post_detail_comment_delete_description),
            positiveText = getString(R.string.post_detail_comment_delete_positive),
            negativeText = getString(R.string.post_detail_comment_delete_negative)
        )
            .apply {
                val commentDeleteDialogListener = object : CommonDialogListener {
                    override fun onPositive() {
                        viewModel.deleteComment(commentDeleteId)
                        dismiss()
                    }

                    override fun onNegative() {
                        dismiss()
                    }
                }
                registerCallbackListener(commentDeleteDialogListener)
            }
    }

    private val replyDeleteDialog = lazy {
        CommonDialogFragment.newInstance(
            title = getString(R.string.post_detail_comment_delete_title),
            description = getString(R.string.post_detail_comment_delete_description),
            positiveText = getString(R.string.post_detail_comment_delete_positive),
            negativeText = getString(R.string.post_detail_comment_delete_negative)
        )
            .apply {
                val commentDeleteDialogListener = object : CommonDialogListener {
                    override fun onPositive() {
                        viewModel.deleteReply(commentDeleteId, replyDeleteId)
                        dismiss()
                    }

                    override fun onNegative() {
                        dismiss()
                    }
                }
                registerCallbackListener(commentDeleteDialogListener)
            }
    }

    private val postActionDialog = lazy {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomsheetDialogBinding =
            FragmentPostViewBottomsheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomsheetDialogBinding.root)

        bottomsheetDialogBinding.tvReportPost.setOnClickListener {
            findNavController().navigateToMyComplaintScreen()
            bottomSheetDialog.dismiss()
        }

        bottomsheetDialogBinding.tvDeletePost.setOnClickListener {
            if (viewModel.user.value == null) return@setOnClickListener

            postDeleteDialog.value.show(childFragmentManager, "postDeleteDialog")
            bottomSheetDialog.dismiss()
        }

        bottomsheetDialogBinding.tvEditPost.setOnClickListener {
            if (viewModel.user.value == null) return@setOnClickListener
            findNavController().navigateToPostEditScreen(
                viewModel.postId.value ?: return@setOnClickListener
            )
            bottomSheetDialog.dismiss()
        }

        viewModel.user.value?.let { user ->
            if (viewModel.post.value?.written?.uid == user.uid) {
                bottomsheetDialogBinding.tvEditPost.visibility = View.VISIBLE
                bottomsheetDialogBinding.tvDeletePost.visibility = View.VISIBLE
            } else {
                bottomsheetDialogBinding.tvEditPost.visibility = View.GONE
                bottomsheetDialogBinding.tvDeletePost.visibility = View.GONE
            }
        }
        bottomSheetDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString("post_id", null)?.let {
            viewModel.updatePostId(it)
        }

        initPostDeletedResult()
    }

    private fun initPostDeletedResult() {
        findNavController().putNavigationResult(NavigationKey.IS_DELETED_POST, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCommentRecyclerView()
        collectPostItem()
        observeCommentRecyclerViewItem()
        setupListener()
        autoScrollKeyboardWithRecyclerView()
        collectPreviewCommentItem()
        initUiStateViewModel()
    }

    private fun initCommentRecyclerView() {
        postDetailAdapter = PostDetailAdapter()
        previewCommentAdapter = PreviewCommentAdapter()
        postConcatAdapter = ConcatAdapter(postDetailAdapter, previewCommentAdapter)
        binding.rvPostView.adapter = postConcatAdapter
        binding.rvPostView.layoutManager = LinearLayoutManager(requireContext())

        val color = ContextCompat.getColor(requireContext(), R.color.bg_thin_gray)
        val height = resources.getDimensionPixelSize(R.dimen.divider_height)
        binding.rvPostView.addItemDecoration(CustomItemDecoration(color, height))
    }

    private fun collectPostItem() {
        lifecycleScope.launch {
            combine(
                viewModel.user,
                viewModel.post,
            ) { user: User?, post: Post? ->
                val headerItem = PostContentItem(user, post)
                postDetailAdapter.updatePostHeader(headerItem)

                if (user == null || post?.id == "null" || post?.id.isNullOrBlank()) {
                    binding.clPostAddComment.visibility = View.GONE
                    return@combine
                }

                binding.clPostAddComment.visibility = View.VISIBLE

                if (post!!.bookmarkUser.contains(user.uid)) {
                    binding.includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark_filled)
                } else {
                    binding.includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark)
                }
            }
                .flowWithLifecycle(lifecycle)
                .collect()
        }
    }

    private fun observeCommentRecyclerViewItem() {
        viewModel.comment.collectLatestWithLifecycle(lifecycle) {
            postDetailAdapter.submitData(it)
        }
    }

    private fun autoScrollKeyboardWithRecyclerView() {
        binding.rvPostView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.rvPostView.scrollBy(0, oldBottom - bottom)
            }
        }
    }

    private fun collectPreviewCommentItem() {
        viewModel.previewCommentItem.collectLatestWithLifecycle(lifecycle) {
            previewCommentAdapter.submitList(it)

            if (it.isNotEmpty()) {
                binding.rvPostView.smoothScrollToPosition(postConcatAdapter.itemCount)
            }
        }
    }

    private fun setupListener() = with(binding) {
        includePostViewAppBar.ivPostBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnPostAddComment.setOnClickListener {
            val comment = etPostAddComment.text.toString()
            if (comment.isEmpty()) {
                Toast.makeText(requireContext(), "댓글을 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addComment(text = comment)
                binding.etPostAddComment.setText("")
                binding.root.hideSoftKeyboard()
            }
        }

        includePostViewAppBar.ivPostBookmark.setOnClickListener {
            viewModel.toggleBookmark()
        }

        includePostViewAppBar.ivPostMore.setOnClickListener {
            postActionDialog.value.show()
        }

        postDetailAdapter.registerCallback(this@PostDetailFragment)
        previewCommentAdapter.registerCallback(this@PostDetailFragment)
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dismissPostDeleteDialog()
        dismissCommentDeleteDialog()
        dismissReplyDeleteDialog()
        dismissPostActionDialog()
    }

    private fun dismissPostDeleteDialog() {
        if (postDeleteDialog.isInitialized()) {
            postDeleteDialog.value.dismiss()
        }
    }

    private fun dismissCommentDeleteDialog() {
        if (commentDeleteDialog.isInitialized()) {
            commentDeleteDialog.value.dismiss()
        }
    }

    private fun dismissReplyDeleteDialog() {
        if (replyDeleteDialog.isInitialized()) {
            replyDeleteDialog.value.dismiss()
        }
    }

    private fun dismissPostActionDialog() {
        if (postActionDialog.isInitialized()) {
            postActionDialog.value.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        postDetailAdapter.unregisterCallback()
        previewCommentAdapter.unregisterCallback()

        if (postDeleteDialog.isInitialized()) {
            postDeleteDialog.value.unregisterCallbackListener()
        }

        if (commentDeleteDialog.isInitialized()) {
            commentDeleteDialog.value.unregisterCallbackListener()
        }

        if (replyDeleteDialog.isInitialized()) {
            replyDeleteDialog.value.unregisterCallbackListener()
        }
    }

    private fun initUiStateViewModel() = with(viewModel) {
        postDetailUiState.collectLatestWithLifecycle(lifecycle) { uiState ->
            when (uiState) {
                PostDetailUiState.ErrorExit -> errorExit(findNavController())
                PostDetailUiState.ErrorMissing -> {
                    errorMissing(findNavController())
                }

                PostDetailUiState.Init -> {}
            }
        }
    }

    override fun deleteReply(commentId: String, replyId: String) {
        commentDeleteId = commentId
        replyDeleteId = replyId
        replyDeleteDialog.value.show(childFragmentManager, "replyDeleteDialog")
    }

    override fun deleteItem(comment: Comment) {
        val user = viewModel.user.value ?: return

        if (comment.written.uid == user.uid) {
            commentDeleteId = comment.id
            commentDeleteDialog.value.show(childFragmentManager, "commentDeleteDialog")
        }
    }

    override fun navigateToUserProfile(uid: String) {
        findNavController().navigateToUserProfileScreen(uid)
    }

    override fun commentLikeClick(comment: Comment) {
        viewModel.toggleCommentLike(comment)
    }

    override fun clickPostLike(post: Post) {
        viewModel.toggleLikeToPost()
    }

    override fun clickReplyLike(reply: Reply) {
        viewModel.toggleReplyLike(reply)
    }

    override fun navigateToReply(comment: Comment) {
        findNavController().navigateToReplyScreen(
            postId = comment.parent.postId,
            commentId = comment.id
        )
    }
}