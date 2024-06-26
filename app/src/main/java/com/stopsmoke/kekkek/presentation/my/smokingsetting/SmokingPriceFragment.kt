//package com.stopsmoke.kekkek.presentation.my.smokingsetting
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.widget.addTextChangedListener
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.navigation.fragment.findNavController
//import com.stopsmoke.kekkek.R
//import com.stopsmoke.kekkek.databinding.FragmentSmokingPriceBinding
//import com.stopsmoke.kekkek.invisible
//import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class SmokingPriceFragment : Fragment() {
//    private var _binding: FragmentSmokingPriceBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: SmokingSettingViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        _binding = FragmentSmokingPriceBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initView()
//        observeEditText()
//        observeUiState()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        activity?.invisible()
//    }
//
//    private fun initView() = with(binding) {
//        btnResetingOnboardingNext.setOnClickListener {
//            findNavController().navigate(R.id.action_resetting_onboarding_smoking_price_to_resetting_onboarding_smoking_finish)
//        }
//    }
//
//    private fun observeUiState() {
//        viewModel.packPriceUiState.collectLatestWithLifecycle(lifecycle) {
//            when (it) {
//                is SmokingSettingUiState.Error -> {
//                    binding.btnResetingOnboardingNext.isEnabled = false
//                    binding.tvSmokingPriceWarning.visibility = View.VISIBLE
//                }
//
//                is SmokingSettingUiState.Loading -> {
//                    binding.btnResetingOnboardingNext.isEnabled = false
//                    binding.tvSmokingPriceWarning.visibility = View.GONE
//                }
//
//                is SmokingSettingUiState.Success -> {
//                    binding.btnResetingOnboardingNext.isEnabled = true
//                    binding.tvSmokingPriceWarning.visibility = View.GONE
//                }
//            }
//        }
//    }
//
//    private fun observeEditText() {
//        binding.etOnboardingPrice.addTextChangedListener {
//            viewModel.updateSmokingPackPrice(it.toString())
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}