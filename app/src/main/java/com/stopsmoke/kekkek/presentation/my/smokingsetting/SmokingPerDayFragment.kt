package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingPerDayBinding


class SmokingPerDayFragment : Fragment() {
    private var _binding: FragmentSmokingPerDayBinding? = null
    private val binding get() = _binding!!

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
        btnResetingOnboardingNext.setOnClickListener {
            findNavController().navigate(R.id.action_resetting_onboarding_smoking_per_day_to_resetting_onboarding_smoking_per_pack)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}