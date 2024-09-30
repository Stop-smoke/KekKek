package com.agvber.kekkek.presentation.settings.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.agvber.kekkek.databinding.ProfileEditNicknameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditNameDialogFragment(private val name: String) : DialogFragment() {
    private var _binding: ProfileEditNicknameBinding? = null
    private val binding get() = _binding!!

    private var job: Job? = null

    private val viewModel: SettingsProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileEditNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        etEditName.setText(name)

        etEditName.addTextChangedListener {
            tvEditDescription.visibility = View.INVISIBLE
        }

        tvEditNameYes.setOnClickListener {
            if (etEditName.text.length > 10) {
                tvEditDescription.visibility = View.VISIBLE
                tvEditDescription.text = "10글자를 초과하시면 안됩니다!"
            } else {
                viewModel.nameDuplicateInspection(etEditName.text.toString())
            }
        }

        tvEditNameNo.setOnClickListener {
            dismiss()
        }
    }

    private fun initViewModel() = with(viewModel) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            nameDuplicationInspectionResult.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { duplicateInspectionResult ->
                    onBindDuplicateInspectionResult(duplicateInspectionResult)
                }
        }
    }

    private fun onBindDuplicateInspectionResult(duplicateInspectionResult: EditNameState) =
        with(binding) {
            when (duplicateInspectionResult) {
                EditNameState.Duplication -> {
                    tvEditDescription.text = "이미 존재하는 이름이에요!"
                    tvEditDescription.visibility = View.VISIBLE
                }

                EditNameState.Success -> {
                    viewModel.setUserDataForName(etEditName.text.toString(),
                        onComplete = {
                            dismiss()
                        })
                }

                EditNameState.Empty -> {
                    tvEditDescription.text = "이름을 입력해주세요!"
                    tvEditDescription.visibility = View.VISIBLE
                }

                else -> {}
            }
            viewModel.setNameDuplicationInspectionResult(EditNameState.Input)
        }


    override fun onDestroy() {
        _binding = null
        job?.cancel()
        job=null
        super.onDestroy()
    }
}
