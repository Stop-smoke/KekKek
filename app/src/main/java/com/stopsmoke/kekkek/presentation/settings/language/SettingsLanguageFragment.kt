package com.stopsmoke.kekkek.presentation.settings.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.FragmentSettingsLanguageBinding

class SettingsLanguageFragment : Fragment() {

    private var _binding: FragmentSettingsLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}