package com.stopsmoke.kekkek.presentation.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentErrorMissingBinding
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.visible

class ErrorMissingFragment : Fragment() {

    private var _binding: FragmentErrorMissingBinding? = null
    private val binding: FragmentErrorMissingBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.invisible()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorMissingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivMissingError.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.includeErrorMissingAppBar.ivErrorMissingBack.setOnClickListener {
            findNavController().popBackStack()
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