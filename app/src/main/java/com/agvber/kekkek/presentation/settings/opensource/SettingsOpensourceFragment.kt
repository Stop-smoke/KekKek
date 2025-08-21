package com.agvber.kekkek.presentation.settings.opensource

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.agvber.kekkek.databinding.FragmentSettingsOpensourceBinding

class SettingsOpensourceFragment : Fragment() {

    private lateinit var binding: FragmentSettingsOpensourceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsOpensourceBinding.inflate(inflater, container, false)
        setupLicenseInfo()
        return binding.root
    }

    private fun setupLicenseInfo() {
        startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
    }
}