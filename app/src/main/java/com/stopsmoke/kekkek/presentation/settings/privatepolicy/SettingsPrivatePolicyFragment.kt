package com.stopsmoke.kekkek.presentation.settings.privatepolicy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentSettingsPrivatePolicyBinding
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.visible

class SettingsPrivatePolicyFragment : Fragment() {

    private var _binding: FragmentSettingsPrivatePolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsPrivatePolicyBinding.inflate(inflater, container, false)

        binding.includeSettingsPrivatePolicyAppBar.ivSettingsPrivatePolicyBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.webviewSupport) {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true // 노션 출력시 필요: DOM 스토리지 활성화
            settings.databaseEnabled = true // 노션 출력시 필요: 데이터베이스 저장소 활성화
            loadUrl("https://stopsmoke.notion.site/1862f151530b46cc8d615c8ef9b636ba")
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