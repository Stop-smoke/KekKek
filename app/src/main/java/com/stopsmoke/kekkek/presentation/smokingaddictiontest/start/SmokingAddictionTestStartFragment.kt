package com.stopsmoke.kekkek.presentation.smokingaddictiontest.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentSmokingAddictionTestStartBinding
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.smokingaddictiontest.navigateToSmokingAddictionTestScreen

class SmokingAddictionTestStartFragment : Fragment() {

    private var _binding: FragmentSmokingAddictionTestStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSmokingAddictionTestStartBinding.inflate(inflater, container, false)
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
        binding.btnTestStart.setOnClickListener {
            findNavController().navigateToSmokingAddictionTestScreen()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}