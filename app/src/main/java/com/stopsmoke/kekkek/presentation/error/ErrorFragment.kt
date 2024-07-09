package com.stopsmoke.kekkek.presentation.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentErrorServerEtcBinding
import com.stopsmoke.kekkek.presentation.invisible

class ErrorFragment: Fragment() {
    private var _binding: FragmentErrorServerEtcBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentErrorServerEtcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() = with(binding){
        includeErrorServerEtcAppBar.ivErrorServerEtcBack.setOnClickListener {
            val navController = findNavController()

            val isPopped = navController.popBackStack()

            if (!isPopped) {
                requireActivity().finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}