package com.stopsmoke.kekkek.presentation.reply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.databinding.FragmentReplyBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.reply.callback.ReplyCallback
import com.stopsmoke.kekkek.presentation.reply.callback.ReplyDialogCallback
import com.stopsmoke.kekkek.presentation.reply.dialog.ReplyDeleteDialogFragment
import com.stopsmoke.kekkek.presentation.userprofile.navigateToUserProfileScreen
import com.stopsmoke.kekkek.presentation.utils.CustomItemDecoration
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyFragment : Fragment(), ReplyCallback, ReplyDialogCallback, ErrorHandle {
    private var _binding: FragmentReplyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReplyViewModel by viewModels()

    private val replyAdapter: ReplyAdapter by lazy {
        ReplyAdapter(viewModel, viewLifecycleOwner)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString("post_id")?.let {
            viewModel.updatePostId(it)
        }

        arguments?.getString("comment_id")?.let {
            viewModel.updateCommentId(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initViewModel()
        initEditTextListener()
        setBackBtn()
    }

    private fun initRecyclerView() = with(binding) {
        rvReply.layoutManager = LinearLayoutManager(requireContext())
        val color = ContextCompat.getColor(requireContext(), R.color.bg_thin_gray)
        val height = resources.getDimensionPixelSize(R.dimen.divider_height)
        rvReply.addItemDecoration(CustomItemDecoration(color, height))
        rvReply.adapter = replyAdapter
        replyAdapter.registerCallback(this@ReplyFragment)
    }

    private fun initViewModel() = with(viewModel) {
        reply.collectLatestWithLifecycle(lifecycle) { replyResult ->
            when (replyResult) {
                is Result.Error -> {
                    replyResult.exception?.printStackTrace()
                    errorExit(findNavController())
                }

                Result.Loading -> {}
                is Result.Success -> replyAdapter.submitData(replyResult.data)
            }
        }

        comment.collectLatestWithLifecycle(viewLifecycleOwner.lifecycle) { commentResult ->
            when (commentResult) {
                is Result.Error -> {
                    commentResult.exception?.printStackTrace()
                    errorExit(findNavController())
                }

                Result.Loading -> {}
                is Result.Success -> replyAdapter.updateComment()
                null -> {}
            }
        }
    }

    private fun initEditTextListener() = with(binding) {
        binding.btnReplyComment.setOnClickListener {
            val reply = etReplyComment.text.toString()
            if (reply.isEmpty()) {
                Toast.makeText(requireContext(), "댓글을 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addReply(reply = reply)
                replyAdapter.refresh()
                binding.etReplyComment.setText("")
                binding.root.hideSoftKeyboard()
            }
        }

        hideEditText()
    }

    private fun hideEditText() {
        viewModel.user.collectLatestWithLifecycle(lifecycle) { user ->
            if (user is User.Guest) {
                binding.clReplyComment.visibility = View.GONE
            }
        }
    }

    private fun setBackBtn() {
        binding.includeFragmentReplyAppBar.ivPostCommentBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun View.hideSoftKeyboard() {
        val inputMethodManager =
            ContextCompat.getSystemService(context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.visible()
        _binding = null
        replyAdapter.unregisterCallback()
    }


    override fun deleteItem(reply: Reply) {
        when (viewModel.user.value) {
            is User.Error -> {}
            User.Guest -> {}
            is User.Registered -> {
                if ((viewModel.user.value as User.Registered).uid == reply.written.uid) {
                    showDeleteDialog(reply)
                }
            }

            null -> {}
        }
    }

    override fun updateReply(updateReply: Reply) {
        viewModel.updateReply(updateReply)
        replyAdapter.refresh()
    }

    override fun commentLikeClick(comment: Comment) {
        viewModel.commentLikeClick(comment)
    }


    override fun navigateToUserProfile(uid: String) {
        findNavController().navigateToUserProfileScreen(uid)
    }

    private fun showDeleteDialog(reply: Reply) {
        val replyDeleteDialog = ReplyDeleteDialogFragment(this@ReplyFragment, reply)
        replyDeleteDialog.show(childFragmentManager, "replyDeleteDialog")
    }

    override fun deleteReply(reply: Reply) {
        viewModel.deleteReply(reply)
        replyAdapter.refresh()
    }
}