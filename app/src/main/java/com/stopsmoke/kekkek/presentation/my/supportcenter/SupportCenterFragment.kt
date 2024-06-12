package com.stopsmoke.kekkek.presentation.my.supportcenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentSupportCenterBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.visible

class SupportCenterFragment : Fragment() {

    private var _binding: FragmentSupportCenterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportCenterBinding.inflate(inflater, container, false)

        binding.includeSupportCenterAppBar.ivSupportCenterBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.viewSupportCenter) {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
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