package com.stopsmoke.kekkek.presentation.settings.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.stopsmoke.kekkek.databinding.ProfileEditIntroductionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditIntroductionDialogFragment(private val introduction: String) : DialogFragment() {
    private var _binding: ProfileEditIntroductionBinding? = null
    private val binding get() = _binding!!

    private var job: Job? = null

    private val viewModel: SettingsProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileEditIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding) {
        etEditIntroduction.setText(introduction)

        etEditIntroduction.addTextChangedListener {
            tvIntroductionDescription.visibility = View.INVISIBLE
        }

        tvEditIntroductionYes.setOnClickListener {
            if (etEditIntroduction.text.length > 50) {
                tvIntroductionDescription.visibility = View.VISIBLE
                tvIntroductionDescription.text = "50글자를 초과하시면 안됩니다!"
            } else {
                viewModel.setUserDataForIntroduction(etEditIntroduction.text.toString(),
                    onComplete = {
                        dismiss()
                    })
            }
        }

        tvEditIntroductionNo.setOnClickListener {
            dismiss()
        }
    }


    override fun onDestroy() {
        _binding = null
        job?.cancel()
        super.onDestroy()
    }

}