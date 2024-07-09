package com.stopsmoke.kekkek.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentOnboardingFinishBinding
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.onboarding.model.OnboardingUiState

class OnboardingFinishFragment : Fragment() {

    private var _binding: FragmentOnboardingFinishBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateUserData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOnboardingFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onboardingUiState.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                OnboardingUiState.LoadFail -> {
                    findNavController().popBackStack("authentication_screen", false)
                    Toast.makeText(requireContext(), "유저 설정 과정에서 문제가 발생했어요!", Toast.LENGTH_LONG).show()
                }
                OnboardingUiState.Loading -> {  }
                OnboardingUiState.Success -> {
                    findNavController().navigate("home") {
                        popUpTo(findNavController().graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}