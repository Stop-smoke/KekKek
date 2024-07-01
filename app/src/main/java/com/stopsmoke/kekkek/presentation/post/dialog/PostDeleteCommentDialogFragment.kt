package com.stopsmoke.kekkek.presentation.post.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.stopsmoke.kekkek.databinding.FragmentCommonDialogBinding
import com.stopsmoke.kekkek.presentation.post.PostViewModel
import com.stopsmoke.kekkek.presentation.post.callback.PostCommentDialogCallback

class PostDeleteCommentDialogFragment(
    private val callback: PostCommentDialogCallback,
    private val commentId: String
):DialogFragment() {
    private var _binding: FragmentCommonDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommonDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        initListener()
    }

    private fun bind() = with(binding){
        tvDialogTitle.text = "댓글 삭제"
        tvDialogContent.text = "댓글을 삭제하시겠습니까?"

        btnDialogFinish.text = "예"
        btnDialogCancel.text = "아니요"
    }

    private fun initListener() = with(binding){
        btnDialogCancel.setOnClickListener {
            dismiss()
        }

        btnDialogFinish.setOnClickListener {
            callback.deleteComment(commentId)
            dismiss()
        }
    }
}