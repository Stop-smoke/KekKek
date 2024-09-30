package com.agvber.kekkek.presentation.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agvber.kekkek.databinding.FragmentErrorServerEtcBinding
import com.agvber.kekkek.presentation.invisible
import com.agvber.kekkek.presentation.visible

class ErrorServerEtcFragment : Fragment() {

    private var _binding: FragmentErrorServerEtcBinding? = null
    private val binding: FragmentErrorServerEtcBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.invisible()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorServerEtcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivServerEtcError.setOnClickListener {
            activity?.finishAffinity()
        }

        binding.includeErrorServerEtcAppBar.ivErrorServerEtcBack.setOnClickListener {
            activity?.finishAffinity()
        }
    }


    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.visible()
        _binding = null
    }
}