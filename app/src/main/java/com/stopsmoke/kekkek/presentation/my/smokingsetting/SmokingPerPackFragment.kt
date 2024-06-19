package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingPerPackBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SmokingPerPackFragment : Fragment() {
    private var _binding: FragmentSmokingPerPackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SmokingSettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSmokingPerPackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeEditText()
        observeUiState()
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    private fun initView() = with(binding) {
        btnResetingOnboardingNext.setOnClickListener {
            findNavController().navigate(R.id.action_resetting_onboarding_smoking_per_pack_to_resetting_onboarding_smoking_price)
        }
    }

    private fun observeEditText() {
        binding.etOnboardingPerpack.addTextChangedListener {
            viewModel.updateSmokingPerPack(it.toString())
        }
    }

    private fun observeUiState() {
        viewModel.perPackUiState.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is SmokingSettingUiState.Error -> {
                    binding.tvSmokingPerPackWarning.visibility = View.VISIBLE
                    binding.btnResetingOnboardingNext.isEnabled = false
                }

                is SmokingSettingUiState.Loading -> {
                    binding.btnResetingOnboardingNext.isEnabled = false
                    binding.tvSmokingPerPackWarning.visibility = View.GONE
                }

                is SmokingSettingUiState.Success -> {
                    binding.tvSmokingPerPackWarning.visibility = View.GONE
                    binding.btnResetingOnboardingNext.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}