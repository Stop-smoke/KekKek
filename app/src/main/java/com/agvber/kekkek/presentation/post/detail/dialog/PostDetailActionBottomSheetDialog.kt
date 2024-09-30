package com.agvber.kekkek.presentation.post.detail.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.agvber.kekkek.databinding.FragmentPostViewBottomsheetDialogBinding
import com.agvber.kekkek.presentation.my.complaint.navigateToMyComplaintScreen

class PostDetailActionBottomSheetDialog: DialogFragment() {

    private var _binding: FragmentPostViewBottomsheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostViewBottomsheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvEditPost.setOnClickListener {
            dismiss()
        }

        binding.tvReportPost.setOnClickListener {
            Toast.makeText(requireContext(), "게시물 신고를 작성해주세요.", Toast.LENGTH_SHORT).show()
            findNavController().navigateToMyComplaintScreen()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}