package com.stopsmoke.kekkek.presentation.home.tip

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
import com.stopsmoke.kekkek.databinding.FragmentHomeTipBinding
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.visible

class HomeTipFragment : Fragment() {

    private var _binding: FragmentHomeTipBinding? = null
    private val binding get() = _binding!!
    private lateinit var callback: OnBackPressedCallback


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webviewHometip.canGoBack()) {
                    binding.webviewHometip.goBack()
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
        _binding = FragmentHomeTipBinding.inflate(inflater, container, false)

        binding.includeHomeTipAppBar.ivHomeTipBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.webviewHometip) {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true // 노션 출력시 필요: DOM 스토리지 활성화
            settings.databaseEnabled = true // 노션 출력시 필요: 데이터베이스 저장소 활성화
            loadUrl("https://stopsmoke.notion.site/fde158991fe84b5dbf6bf1aff73867bc?pvs=4")
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

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}