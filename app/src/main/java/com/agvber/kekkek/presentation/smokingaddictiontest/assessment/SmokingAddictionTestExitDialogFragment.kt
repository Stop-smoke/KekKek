package com.agvber.kekkek.presentation.smokingaddictiontest.assessment

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
import com.agvber.kekkek.databinding.FragmentCommonDialogBinding
import com.agvber.kekkek.presentation.home.popBackStackInclusiveHome
import com.agvber.kekkek.presentation.smokingaddictiontest.SmokingAddictionTestViewModel

class SmokingAddictionTestExitDialogFragment : DialogFragment() {

    private var _binding: FragmentCommonDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<SmokingAddictionTestViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCommonDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnDialogCancel.setOnClickListener {
            dismiss()
        }
        binding.btnDialogFinish.setOnClickListener {
            findNavController().popBackStackInclusiveHome()
            viewModel.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}