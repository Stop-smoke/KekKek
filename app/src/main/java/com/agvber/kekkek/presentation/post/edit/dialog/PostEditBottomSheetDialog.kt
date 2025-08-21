package com.agvber.kekkek.presentation.post.edit.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.agvber.kekkek.databinding.FragmentPostEditBottomsheetDialogBinding

class PostEditBottomSheetDialog(private val listener: (String) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: FragmentPostEditBottomsheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostEditBottomsheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryFree.setOnClickListener {
            listener("자유 게시판")
            dismiss()
        }
        binding.categoryReview.setOnClickListener {
            listener("금연 보조제 후기")
            dismiss()
        }
        binding.categoryPromise.setOnClickListener {
            listener("금연 다짐")
            dismiss()
        }
        binding.categorySuccess.setOnClickListener {
            listener("금연 성공 후기")
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
