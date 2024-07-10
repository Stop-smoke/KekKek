package com.stopsmoke.kekkek.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentOnboardingIntroduceBinding
import com.stopsmoke.kekkek.presentation.invisible


class OnboardingIntroduceFragment : Fragment() {

    private var _binding: FragmentOnboardingIntroduceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.invisible()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardingIntroduceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOnboardingNext.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding_introduce_to_onboarding_name)
        }
        binding.lottieAnimationView.setAnimation(R.raw.lottie_kekkek_smile_2x)
        binding.lottieAnimationView.playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}