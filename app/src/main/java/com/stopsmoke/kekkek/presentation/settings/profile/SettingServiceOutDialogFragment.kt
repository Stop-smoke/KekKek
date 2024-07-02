package com.stopsmoke.kekkek.presentation.settings.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.stopsmoke.kekkek.databinding.FragmentCommonDialogBinding
import com.stopsmoke.kekkek.presentation.settings.SettingsViewModel

class SettingServiceOutDialogFragment : DialogFragment() {

    private val viewModel: SettingsViewModel by activityViewModels()

    private var _binding: FragmentCommonDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommonDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() = with(binding) {
        tvDialogTitle.text = "서비스 계정을 탈퇴하시겠습니까?"
        tvDialogContent.text = "탈퇴하기를 누르시면 지금까지 작성한 글과 댓글이 사라지며 계정을 복구할 수 없습니다. 그래도 정말 탈퇴하시겠습니까?"
        btnDialogFinish.text = "탈퇴하기"
    }

    private fun initListener() = with(binding) {
        btnDialogCancel.setOnClickListener {
            dismiss()
        }
        btnDialogFinish.setOnClickListener {
            viewModel.withdraw()
            dismiss()
        }
    }
}
