package com.stopsmoke.kekkek.presentation.post.reply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentReplyBinding
import com.stopsmoke.kekkek.presentation.CustomItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyFragment : Fragment() {
    private var _binding: FragmentReplyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReplylViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecyclerView() = with(binding){

        rvReply.layoutManager = LinearLayoutManager(requireContext())
//        rvReply.adapter =

        val color = ContextCompat.getColor(requireContext(), R.color.bg_thin_gray)
        val height = resources.getDimensionPixelSize(R.dimen.divider_height)
        rvReply.addItemDecoration(CustomItemDecoration(color, height))
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}