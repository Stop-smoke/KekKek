package com.stopsmoke.kekkek.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentOnboardingFinishBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.collectLatest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnboardingFinishFragment : Fragment() {

    private var _binding: FragmentOnboardingFinishBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

        lifecycleScope.launch {
            delay(1500)
            viewModel.onboardingUiState.collectLatest(lifecycleScope) {
                findNavController().navigate("home")  {
                    popUpTo(findNavController().graph.id) {
                        inclusive = true
                    }
                }
            }

            viewModel.updateUserData()
        }
        }


//    override fun onStop() {
//        super.onStop()
//        activity?.visible()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}