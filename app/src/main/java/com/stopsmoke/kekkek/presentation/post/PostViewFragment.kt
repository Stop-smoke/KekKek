package com.stopsmoke.kekkek.presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostViewBinding
import com.stopsmoke.kekkek.databinding.FragmentPostViewBottomsheetDialogBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.community.CommunityViewModel
import com.stopsmoke.kekkek.presentation.toResourceId
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

    private lateinit var postCommentAdapter: PostCommentAdapter

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
        initPostView()
        setupListener()
        initCommentRecyclerView()
        observeCommentRecyclerViewItem()
        observeCommentCount()

        binding.ivPostPoster.setOnClickListener {
            val post = viewModel.post.value ?: return@setOnClickListener

            findNavController().navigate(
                R.id.action_post_view_to_user_profile, bundleOf(
                    "uid" to (post.written.uid)
                )
            )
        }

        binding.clPostViewHeart.setOnClickListener {
            viewModel.toggleLikeToPost()
        }

        lifecycleScope.launch {
            combine(viewModel.post, viewModel.user) { post, user ->
                if (user !is User.Registered) return@combine
                if (post == null) return@combine

                if (user.uid in viewModel.post.value!!.likeUser) {
                    binding.ivPostHeart.setImageResource(R.drawable.ic_heart_filled)
                } else {
                    binding.ivPostHeart.setImageResource(R.drawable.ic_heart)
                }

                if (post.bookmarkUser.contains(user.uid)) {
                    binding.includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark_filled)
                } else {
                    binding.includePostViewAppBar.ivPostBookmark.setImageResource(R.drawable.ic_bookmark)
                }
            }
                .collect()
        }

        postCommentAdapter.registerCallback(this)
    }

    private fun observeCommentCount() {
        viewModel.commentCount.collectLatestWithLifecycle(lifecycle) {
            binding.tvPostCommentNum.text = it.toString()
        }
    }

    private fun observeCommentRecyclerViewItem() {
        viewModel.comment.collectLatestWithLifecycle(lifecycle) {
            postCommentAdapter.submitData(it)
        }
    }

    private fun initCommentRecyclerView() {
        postCommentAdapter = PostCommentAdapter()
        binding.rvPostComment.adapter = postCommentAdapter
        binding.rvPostComment.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showCommentDeleteDialog(postId : String) {
        AlertDialog.Builder(requireContext())
            .setTitle("댓글 삭제")
            .setMessage("댓글을 삭제하시겠습니까?")
            .setPositiveButton("예") { dialog, _ ->
                viewModel.deleteComment(postId)
                postCommentAdapter.refresh()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun initPostView() = with(binding) {
        viewModel.post.collectLatestWithLifecycle(lifecycle) { post ->
            if (post == null) return@collectLatestWithLifecycle

            tvPostPosterNickname.text = post.written.name
            tvPostPosterRanking.text = "랭킹 ${post.written.ranking}위"
            tvPostHour.text = post.createdElapsedDateTime.toResourceId(requireContext())

            tvPostTitle.text = post.title
            tvPostDescription.text = post.text
            tvPostHeartNum.text = post.likeUser.size.toString()
            tvPostCommentNum.text = post.commentCount.toString()
            tvPostViewNum.text = post.views.toString()
            initWrittenProfileImage(post.written.profileImage)
        }
    }

    private fun initWrittenProfileImage(profileImage: ProfileImage) {
        when (profileImage) {
            is ProfileImage.Web -> binding.ivPostPoster.load(profileImage.url)
            is ProfileImage.Default -> binding.ivPostView.setImageResource(R.drawable.ic_user_profile_test)
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
                postCommentAdapter.refresh()
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

        bottomsheetDialogBinding.tvDeletePost.setOnClickListener {
            if (viewModel.user.value !is User.Registered) return@setOnClickListener

            showDeleteConfirmationDialog()
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
                    bottomsheetDialogBinding.tvDeletePost.visibility = View.VISIBLE
                } else {
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
                    communityViewModel.setPostDeleted(true)
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
        postCommentAdapter.unregisterCallback()
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
}