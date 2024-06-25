package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.exception.StartWithZeroException
import com.stopsmoke.kekkek.databinding.FragmentSmokingPerDayBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SmokingPerDayFragment : Fragment() {
    private var _binding: FragmentSmokingPerDayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SmokingSettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSmokingPerDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeEditText()
        observeUiState()
    }

    private fun initView() = with(binding) {
        btnResetingOnboardingNext.setOnClickListener {
            findNavController().navigate(R.id.action_resetting_onboarding_smoking_per_day_to_resetting_onboarding_smoking_per_pack)
        }
    }

    private fun observeEditText() {
        binding.etOnboardingPerday.addTextChangedListener {
            viewModel.updateSmokingPerDay(it.toString())
        }
    }

    private fun observeUiState() {
        viewModel.perDayUiState.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is SmokingSettingUiState.Error -> {
                    if(it.t is StartWithZeroException) {
                        binding.tvSmokingPerDayWarning.text = "0으로 시작하면 안됩니다."
                        binding.tvSmokingPerDayWarning.visibility = View.VISIBLE
                    } else {
                        binding.tvSmokingPerDayWarning.text = "값을 입력해주세요."
                        binding.tvSmokingPerDayWarning.visibility = View.VISIBLE
                        binding.btnResetingOnboardingNext.isEnabled = false
                    }
                }

                is SmokingSettingUiState.Loading -> {
                    val drawable = binding.btnResetingOnboardingNext.background as GradientDrawable
                    drawable.setColor(ContextCompat.getColor(requireContext(), R.color.gray_lightgray1))
                    binding.tvSmokingPerDayWarning.visibility = View.GONE
                    binding.btnResetingOnboardingNext.isEnabled = false
                }

                is SmokingSettingUiState.Success -> {
                    val drawable = binding.btnResetingOnboardingNext.background as GradientDrawable
                    drawable.setColor(ContextCompat.getColor(requireContext(), R.color.primary_blue))
                    binding.tvSmokingPerDayWarning.visibility = View.GONE
                    binding.btnResetingOnboardingNext.isEnabled = true
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}