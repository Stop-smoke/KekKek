package com.stopsmoke.kekkek.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentOnboardingPriceBinding
import com.stopsmoke.kekkek.presentation.invisible


class OnboardingPriceFragment : Fragment() {

    private var _binding: FragmentOnboardingPriceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOnboardingNext.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding_price_to_onboarding_finish)
        }

        binding.etOnboardingPrice.addTextChangedListener {
            if (it.isNullOrBlank()) {
                binding.btnOnboardingNext.isEnabled = false
                return@addTextChangedListener
            }
            binding.btnOnboardingNext.isEnabled = true
            viewModel.updateCigarettePricePerPack(it.toString().toIntOrNull() ?: 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}