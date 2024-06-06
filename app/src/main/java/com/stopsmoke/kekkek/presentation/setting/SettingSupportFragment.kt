package com.stopsmoke.kekkek.presentation.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentSettingSupportBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.visible

class SettingSupportFragment : Fragment() {

    private var _binding: FragmentSettingSupportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingSupportBinding.inflate(inflater, container, false)

        binding.ivSupportBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.webviewSupport) {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            loadUrl("https://limheejin.tistory.com")
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onPause() {
        super.onPause()
        activity?.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}