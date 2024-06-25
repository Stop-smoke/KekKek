package com.stopsmoke.kekkek.presentation.smokingaddictiontest.assessment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingAddictionTestExitDialogBinding
import com.stopsmoke.kekkek.presentation.smokingaddictiontest.SmokingAddictionTestViewModel

class SmokingAddictionTestExitDialogFragment : DialogFragment() {

    private var _binding: FragmentSmokingAddictionTestExitDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<SmokingAddictionTestViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            isCancelable = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSmokingAddictionTestExitDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnFinish.setOnClickListener {
            findNavController().popBackStack("home", false)
            viewModel.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}