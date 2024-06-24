package com.stopsmoke.kekkek.presentation.post.reply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentReplyBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.CustomItemDecoration
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.post.PostCommentCallback
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyFragment : Fragment(), PostCommentCallback {
    private var _binding: FragmentReplyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReplyViewModel by viewModels()

    private val replyAdapter: ReplyAdapter by lazy {
        ReplyAdapter(viewModel, viewLifecycleOwner)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getParcelable<ReplyIdItem>("replyIdItem")?.let {
            viewModel.setReplyId(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initViewModel()
        initEditTextListener()
    }

    private fun initRecyclerView() = with(binding){
        rvReply.layoutManager = LinearLayoutManager(requireContext())
        val color = ContextCompat.getColor(requireContext(), R.color.bg_thin_gray)
        val height = resources.getDimensionPixelSize(R.dimen.divider_height)
        rvReply.addItemDecoration(CustomItemDecoration(color, height))
        rvReply.adapter = replyAdapter
        replyAdapter.registerCallback(this@ReplyFragment)
    }

    private fun initViewModel() = with(viewModel){
        reply.collectLatestWithLifecycle(lifecycle){
            replyAdapter.submitData(it)
        }
    }

    private fun initEditTextListener() = with(binding){
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

    private fun hideEditText(){
        viewModel.user.collectLatestWithLifecycle(lifecycle){user ->
            if(user is User.Guest){
                binding.clReplyComment.visibility = View.GONE
            }
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

    override fun deleteItem(comment: Comment) {}

    override fun navigateToUserProfile(uid: String) {
//        findNavController().navigate(
//            resId = R.id.action_post_view_to_user_profile,
//            args = bundleOf("uid" to uid)
//        )
    }

    override fun commentLikeClick(reply: Reply) {}

    override fun navigateToReply(comment: Comment) {}
}