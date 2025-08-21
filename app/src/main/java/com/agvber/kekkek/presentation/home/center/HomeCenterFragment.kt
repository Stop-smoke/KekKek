package com.agvber.kekkek.presentation.home.center

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agvber.kekkek.databinding.FragmentHomeCenterBinding
import com.agvber.kekkek.presentation.invisible
import com.agvber.kekkek.presentation.visible

class HomeCenterFragment : Fragment() {
    private var _binding: FragmentHomeCenterBinding? = null
    private val binding get() = _binding!!
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webviewHomecenter.canGoBack()) {
                    binding.webviewHomecenter.goBack()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

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
            loadUrl("https://www.nosmokeguide.go.kr/index.do")
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

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}