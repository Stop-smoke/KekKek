package com.stopsmoke.kekkek.presentation.errorhandler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.FragmentErrorServerEtcBinding
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.visible

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.visible()
        _binding = null
    }
}