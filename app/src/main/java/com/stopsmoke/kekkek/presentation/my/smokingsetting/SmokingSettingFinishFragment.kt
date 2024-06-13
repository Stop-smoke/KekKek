package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingPriceBinding
import com.stopsmoke.kekkek.databinding.FragmentSmokingSettingFinishBinding

class SmokingSettingFinishFragment : Fragment() {
    private var _binding: FragmentSmokingSettingFinishBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmokingSettingFinishBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        btnResetingOnboardingNext.setOnClickListener { 
            findNavController().navigate(R.id.action_resetting_onboarding_smoking_finish_to_my_page)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}