package com.stopsmoke.kekkek.presentation.post

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostViewBinding
import com.stopsmoke.kekkek.databinding.FragmentPostViewBottomsheetDialogBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.CustomItemDecoration
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.community.CommunityViewModel
import com.stopsmoke.kekkek.presentation.isNetworkAvailable
import com.stopsmoke.kekkek.presentation.post.reply.ReplyIdItem
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PostViewFragment : Fragment(), PostCommentCallback {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by activityViewModels()

    private lateinit var postViewAdapter: PostViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString("post_id", null)?.let {
            viewModel.updatePostId(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPostViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        initCommentRecyclerView()
        observeCommentRecyclerViewItem()
        observeBookmarkState()
        postViewAdapter.registerCallback(this)
    }

    private fun observeBookmarkState() = lifecycleScope.launch {
        combine(viewModel.post, viewModel.user) { post, user ->
            if (user !is User.Registered) return@combine
            if (post == null) return@combine

            if (post.bookmarkUser.contains(user.uid)) {
                binding.includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark_filled)
            } else {
                binding.includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark)
            }
        }
            .flowWithLifecycle(lifecycle)
            .collect()
    }

    private fun observeCommentRecyclerViewItem() {
        viewModel.comment.collectLatestWithLifecycle(lifecycle) {
            if(!isNetworkAvailable(requireContext())){
                Toast.makeText(requireContext(), "네트워크 연결 오류", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                return@collectLatestWithLifecycle
            }
            postViewAdapter.submitData(it)
        }
    }

    private fun initCommentRecyclerView() {
        postViewAdapter = PostViewAdapter(viewModel = viewModel, lifecycleOwner = viewLifecycleOwner)
        binding.rvPostView.adapter = postViewAdapter
        binding.rvPostView.layoutManager = LinearLayoutManager(requireContext())

        val color = ContextCompat.getColor(requireContext(), R.color.bg_thin_gray)
        val height = resources.getDimensionPixelSize(R.dimen.divider_height)
        binding.rvPostView.addItemDecoration(CustomItemDecoration(color, height))
    }

    private fun showCommentDeleteDialog(postId : String) {
        AlertDialog.Builder(requireContext())
            .setTitle("댓글 삭제")
            .setMessage("댓글을 삭제하시겠습니까?")
            .setPositiveButton("예") { dialog, _ ->
                viewModel.deleteComment(postId)
                postViewAdapter.refresh()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
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
                viewModel.addComment(text = comment, postTitle = viewModel.post.value?.title ?: "")
                postViewAdapter.refresh()
                binding.etPostAddComment.setText("")
                binding.root.hideSoftKeyboard()
            }
        }

        includePostViewAppBar.ivPostBookmark.setOnClickListener {
            viewModel.toggleBookmark()
        }

        includePostViewAppBar.ivPostMore.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomsheetDialogBinding =
            FragmentPostViewBottomsheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomsheetDialogBinding.root)

        bottomsheetDialogBinding.tvReportPost.setOnClickListener {
            findNavController().navigate(R.id.action_post_view_to_my_complaint)
            bottomSheetDialog.dismiss()
        }

        bottomsheetDialogBinding.tvDeletePost.setOnClickListener {
            if (viewModel.user.value !is User.Registered) return@setOnClickListener

            showDeleteConfirmationDialog()
            bottomSheetDialog.dismiss()
        }

        bottomsheetDialogBinding.tvEditPost.setOnClickListener {
            if (viewModel.user.value !is User.Registered) return@setOnClickListener

            findNavController().navigate(
                resId = R.id.action_post_view_to_post_edit,
                args = bundleOf("post_id" to viewModel.postId.value)
            )
            bottomSheetDialog.dismiss()
        }


        // 일단
        when (viewModel.user.value) {
            is User.Error -> {

            }

            User.Guest -> {

            }

            is User.Registered ->
                if (viewModel.post.value?.written?.uid == (viewModel.user.value as User.Registered).uid) {
                    bottomsheetDialogBinding.tvEditPost.visibility = View.VISIBLE
                    bottomsheetDialogBinding.tvDeletePost.visibility = View.VISIBLE
                } else {
                    bottomsheetDialogBinding.tvEditPost.visibility = View.GONE
                    bottomsheetDialogBinding.tvDeletePost.visibility = View.GONE
                }

            null -> {

            }
        }
        bottomSheetDialog.show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("게시글 삭제")
            .setMessage("게시글을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { dialog, _ ->
                viewModel.post.value?.id?.let { postId ->
                    viewModel.deletePost(postId)
                    Toast.makeText(requireContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
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
        postViewAdapter.unregisterCallback()
    }

    private fun View.hideSoftKeyboard() {
        val inputMethodManager = getSystemService(context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
    }


    override fun deleteItem(comment: Comment) {
        when(val user = viewModel.user.value){
            is User.Error -> {

            }
            User.Guest -> {

            }
            is User.Registered ->
                if (comment.written.uid == user.uid) {
                    showCommentDeleteDialog(comment.id)
                }

            null -> {}
        }
    }

    override fun navigateToUserProfile(uid: String) {
        findNavController().navigate(
            resId = R.id.action_post_view_to_user_profile,
            args = bundleOf("uid" to uid)
        )
    }

    override fun commentLikeClick(comment: Comment) {
        viewModel.commentLikeClick(comment)
        postViewAdapter.refresh()
    }

    override fun navigateToReply(comment: Comment) {
        findNavController().navigate(
            resId = R.id.action_post_view_to_reply,
            args = bundleOf("replyIdItem" to ReplyIdItem(commentId = comment.id, postId = comment.parent.postId))
        )
    }
}