package com.stopsmoke.kekkek

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.stopsmoke.kekkek.databinding.FragmentTestBinding
import com.stopsmoke.kekkek.databinding.FragmentTestOnboardingBinding

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