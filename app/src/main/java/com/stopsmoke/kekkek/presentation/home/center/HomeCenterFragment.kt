package com.stopsmoke.kekkek.presentation.home.center

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentHomeCenterBinding
import com.stopsmoke.kekkek.databinding.FragmentSupportCenterBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.visible

class HomeCenterFragment : Fragment() {

    private var _binding: FragmentHomeCenterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCenterBinding.inflate(inflater, container, false)

        binding.includeHomeCenterAppBar.ivHomeCenterBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.webviewHomecenter) {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSdtFqgdIcfTw99XSvOzbsyiOqxTDFkm1_BDqIIqOw2YzLnzxA/viewform")
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