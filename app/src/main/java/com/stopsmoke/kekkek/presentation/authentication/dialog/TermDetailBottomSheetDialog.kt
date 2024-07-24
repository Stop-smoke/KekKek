package com.stopsmoke.kekkek.presentation.authentication.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.DialogTermDetailBinding

class TermDetailBottomSheetDialog(val termType: TermType): BottomSheetDialogFragment() {
    private var _binding: DialogTermDetailBinding? = null
    private val binding get() = _binding!!

    val termUrlMapList = listOf(
        TermType.PRIVATE to requireContext().getString(R.string.term_private_url),
        TermType.SERVICE to requireContext().getString(R.string.term_service_url),
        TermType.COMMUNITY to requireContext().getString(R.string.term_community_url)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTermDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWebView()
    }

    private fun initWebView() {
        val url: String = when(termType) {
            TermType.PRIVATE -> termUrlMapList.find { it.first == TermType.PRIVATE }?.second
            TermType.SERVICE -> termUrlMapList.find { it.first == TermType.SERVICE }?.second
            TermType.COMMUNITY -> termUrlMapList.find { it.first == TermType.COMMUNITY }?.second
        } ?: throw IllegalStateException("URL not found for TermType: $termType")

        with(binding.webviewTermDetail) {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
            loadUrl(url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}