package com.stopsmoke.kekkek.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentOnboardingNameBinding
import com.stopsmoke.kekkek.invisible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OnboardingNameFragment : Fragment() {

    private var _binding: FragmentOnboardingNameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etOnboardingName.addTextChangedListener { editable ->
            if (editable.isNullOrBlank()) {
                binding.btnOnboardingNext.isEnabled = false
                return@addTextChangedListener
            } else binding.btnOnboardingNext.isEnabled = true
            binding.tvOnboardingWarning.visibility = View.INVISIBLE
        }


        initDuplicateInspection()
    }

    private fun initDuplicateInspection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.nameDuplicationInspectionResult.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { duplicateInspectionResult ->
                    if (duplicateInspectionResult == false) {
                        binding.tvOnboardingWarning.visibility = View.VISIBLE
                        binding.tvOnboardingWarning.text = "이미 존재하는 이름이에요!"
                    } else if (duplicateInspectionResult == true) {
                        viewModel.updateUserName(binding.etOnboardingName.text.toString())
                        findNavController().navigate(R.id.action_onboarding_name_to_onboarding_perday)
                        viewModel.setNameDuplicationInspectionResult(null)
                    } else if (duplicateInspectionResult == null) {
                        binding.tvOnboardingWarning.visibility = View.INVISIBLE
                    }
                }
        }

        binding.btnOnboardingNext.setOnClickListener {
            viewModel.nameDuplicateInspection(binding.etOnboardingName.text.toString())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}