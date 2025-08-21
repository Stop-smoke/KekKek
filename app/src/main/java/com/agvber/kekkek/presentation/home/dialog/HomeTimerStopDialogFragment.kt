package com.agvber.kekkek.presentation.home.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.agvber.kekkek.databinding.FragmentHomeStopDialogBinding
import com.agvber.kekkek.presentation.home.HomeViewModel

class HomeTimerStopDialogFragment:DialogFragment() {
    private var _binding:FragmentHomeStopDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeStopDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() = with(binding){
        btnDialogFinish.setOnClickListener {
            viewModel.setStopUserHistory()
            dismiss()
        }

        btnDialogCancel.setOnClickListener {
            dismiss()
        }
    }
}