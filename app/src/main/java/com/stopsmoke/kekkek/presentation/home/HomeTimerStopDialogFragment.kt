package com.stopsmoke.kekkek.presentation.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentCommonDialogBinding

class HomeTimerStopDialogFragment:DialogFragment() {
    private var _binding:FragmentCommonDialogBinding? = null
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
        tvDialogTitle.text = "타이머 초기화"
        tvDialogContent.text = "금연을 중단하시겠습니까?" +
                "\n지금까지 노력하신 시간이 모두 초기화됩니다."

        btnDialogCancel.text = "아니요"
        btnDialogFinish.text = "예"

        ivDialogIcon.setImageResource(R.drawable.ic_timer)
        ivDialogIcon.visibility = View.VISIBLE
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