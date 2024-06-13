package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingPerDayBinding
import com.stopsmoke.kekkek.invisible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SmokingPerDayFragment : Fragment() {
    private var _binding: FragmentSmokingPerDayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SmokingSettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmokingPerDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        etOnboardingPerday.addTextChangedListener {
            tvSmokingPerDayWarning.visibility = View.INVISIBLE
        }
        btnResetingOnboardingNext.setOnClickListener {
            if (etOnboardingPerday.text.toString().isEmpty()) {
                tvSmokingPerDayWarning.visibility = View.VISIBLE
            } else {
                viewModel.setDailyCigarettesSmoked(etOnboardingPerday.text.toString().toInt())
                findNavController().navigate(R.id.action_resetting_onboarding_smoking_per_day_to_resetting_onboarding_smoking_per_pack)
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