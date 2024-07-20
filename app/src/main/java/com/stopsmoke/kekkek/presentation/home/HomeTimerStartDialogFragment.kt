package com.stopsmoke.kekkek.presentation.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.stopsmoke.kekkek.databinding.DialogHomeTimerBinding
import java.time.LocalDateTime

class HomeTimerStartDialogFragment:DialogFragment() {
    private var _binding:DialogHomeTimerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setTitle("날짜와 시간을 선택해주세요.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogHomeTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        initListener()
    }

    private fun bind() = with(binding){

    }


    private fun initListener() = with(binding){
        btnHomeTimerDialogFinish.setOnClickListener {
            val year = binding.datePickerHomeTimerDialog.year
            val month = binding.datePickerHomeTimerDialog.month+1
            val day = binding.datePickerHomeTimerDialog.dayOfMonth

            val hour = binding.timePickerHomeTimerDailog.currentHour
            val minute = binding.timePickerHomeTimerDailog.currentMinute

            val selectDateTime = LocalDateTime.of(year, month, day, hour, minute)
            if(LocalDateTime.now() < selectDateTime){
                Toast.makeText(requireContext(), "현재 시간보다 앞서나갔습니다.", Toast.LENGTH_SHORT).show()
            }else {
                viewModel.setStartUserHistory(selectDateTime)
                dismiss()
            }
        }

        btnHomeTimerDialogCancel.setOnClickListener {
            dismiss()
        }
    }
}