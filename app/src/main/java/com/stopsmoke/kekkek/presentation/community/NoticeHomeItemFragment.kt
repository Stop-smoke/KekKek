package com.stopsmoke.kekkek.presentation.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.stopsmoke.kekkek.PostViewFragment
import com.stopsmoke.kekkek.R

class NoticeHomeItemFragment : Fragment() {
//    private var _binding: ItemCommunityNoticehomeBinding? = null
//    private val binding: ItemCommunityNoticehomeBinding get() = _binding!!
//    private var textList: List<String>? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            textList = it.getStringArrayList("TEXT_LIST")
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = ItemCommunityNoticehomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initView()
//        initListener()
//    }
//
//    private fun initView() = with(binding) {
//        val textViewList = listOf(
//            tvItemCommunityText1,
//            tvItemCommunityText2,
//            tvItemCommunityText3
//        )
//        textList?.let { textList ->
//            val listSize = textList.size
//            textViewList.forEachIndexed { index, textView ->
//                if(index < listSize) textView.text = textList[index]
//                else textView.visibility = View.GONE
//            }
//        }
//    }
//
//    private fun initListener() = with(binding) {
//        tvItemCommunityText1.setOnClickListener {
//            val fragment = PostViewFragment.newInstance(tvItemCommunityText1.text.toString())
//            parentFragmentManager.commit {
//                replace(R.id.main,fragment)
//                addToBackStack(null)
//            }
//        }
//        tvItemCommunityText2.setOnClickListener {
//            val fragment = PostViewFragment.newInstance(tvItemCommunityText2.text.toString())
//            parentFragmentManager.commit {
//                replace(R.id.main, fragment)
//                addToBackStack(null)
//            }
//        }
//        tvItemCommunityText3.setOnClickListener {
//            val fragment = PostViewFragment.newInstance(tvItemCommunityText3.text.toString())
//            parentFragmentManager.commit {
//                replace(R.id.main, fragment)
//                addToBackStack(null)
//            }
//        }
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance(textList: List<String>) =
//            NoticeHomeItemFragment().apply {
//                arguments = Bundle().apply {
//                    putStringArrayList("TEXT_LIST", ArrayList(textList))
//                }
//            }
//    }
}