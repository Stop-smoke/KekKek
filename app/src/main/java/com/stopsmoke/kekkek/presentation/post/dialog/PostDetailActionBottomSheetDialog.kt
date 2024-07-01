package com.stopsmoke.kekkek.presentation.post.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostViewBottomsheetDialogBinding

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
            findNavController().navigate(R.id.action_post_view_to_post_edit)
            dismiss()
        }

        binding.tvReportPost.setOnClickListener {
            Toast.makeText(requireContext(), "게시물 신고를 작성해주세요.", Toast.LENGTH_SHORT).show()
            findNavController().navigate("my_complaint")
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}