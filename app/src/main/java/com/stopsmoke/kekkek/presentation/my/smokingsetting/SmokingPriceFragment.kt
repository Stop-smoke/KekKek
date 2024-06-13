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
import com.stopsmoke.kekkek.databinding.FragmentSmokingPerPackBinding
import com.stopsmoke.kekkek.databinding.FragmentSmokingPriceBinding
import com.stopsmoke.kekkek.invisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SmokingPriceFragment : Fragment() {
    private var _binding: FragmentSmokingPriceBinding?= null
    private val binding get() = _binding!!

    private val viewModel: SmokingSettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmokingPriceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    private fun initView() = with(binding) {
        etOnboardingPrice.addTextChangedListener {
            tvSmokingPriceWarning.visibility = View.INVISIBLE
        }
        btnResetingOnboardingNext.setOnClickListener {
            if (etOnboardingPrice.text.toString().isEmpty()) {
                tvSmokingPriceWarning.visibility = View.VISIBLE
            } else {
                viewModel.setPackPrice(etOnboardingPrice.text.toString().toInt())
                findNavController().navigate(R.id.action_resetting_onboarding_smoking_price_to_resetting_onboarding_smoking_finish)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}