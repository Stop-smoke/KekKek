package com.stopsmoke.kekkek.presentation.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.stopsmoke.kekkek.databinding.FragmentMyBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding: FragmentMyBinding get() = _binding!!

    private val viewModel: MyViewModel by lazy {
        ViewModelProvider(this)[MyViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView()= with(binding){
        val myWritingList = listOf(
            tvMyWriting,
            tvMyComments,
            tvMyBookmark
        )

        myWritingList.forEach { textView ->
            textView.setOnClickListener {  }
        }

        ivMySetting.setOnClickListener {}
    }

    private fun initViewModel() = with(viewModel){
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }
    }


    private fun onBind(myUiState: MyUiState) = with(binding){
        when(myUiState.myLoginUiState){
            is MyLoginStatusState.NeedLoginUiState ->{ // 로그인 필요

            }
            is MyLoginStatusState.LoggedUiState.MyIdLoggedUiState ->{ //로그인 성공
                val myItem: MyItem = myUiState.myLoginUiState.myItem
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            MyFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}