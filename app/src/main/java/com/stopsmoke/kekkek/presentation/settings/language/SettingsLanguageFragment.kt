package com.stopsmoke.kekkek.presentation.settings.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSettingsLanguageBinding
import java.util.Locale

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppBar()
        initRadioButton()
    }

    private fun initAppBar() = with(binding) {
        settingLanguage.tvUserProfileTitle.text = "언어"
        settingLanguage.ivUserProfileBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initRadioButton() = with(binding) {
        radioGroupLanguage.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radio_button_korean -> {
                    setLocale("ko")
                }
                R.id.radio_button_english -> {
                    setLocale("en")
                }
            }
        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = requireContext().resources
        val config = resources.configuration
        config.setLocale(locale)

        parentFragmentManager.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}