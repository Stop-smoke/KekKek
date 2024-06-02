package com.stopsmoke.kekkek.presentation.home.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stopsmoke.kekkek.databinding.FragmentTestOnboardingBinding
import com.stopsmoke.kekkek.invisible

class TestOnBoardingFragment(
    private val callback: () -> Unit
) : Fragment() {

    private var _binding: FragmentTestOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestOnboardingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    private fun setupListener() {
        binding.run {
            btnTestStart.setOnClickListener {
                callback()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}