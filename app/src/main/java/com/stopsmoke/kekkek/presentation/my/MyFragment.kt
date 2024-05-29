package com.stopsmoke.kekkek.presentation.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.stopsmoke.kekkek.databinding.FragmentMyBinding


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

    companion object {
        @JvmStatic
        fun newInstance() =
            MyFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}