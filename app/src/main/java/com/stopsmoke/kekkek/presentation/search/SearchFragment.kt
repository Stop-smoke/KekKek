package com.stopsmoke.kekkek.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var recommendedAdapter: KeywordRecommendedListAdapter

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeRecyclerViewItem()
        clickBackButton()
        watchKeywordEditText()
        binding.tvSearchRecommend.text = "희진님을 위한 추천검색어"
    }

    private fun initRecyclerView() {
        recommendedAdapter = KeywordRecommendedListAdapter()
        binding.rvSearchRecommended.adapter = recommendedAdapter
        binding.rvSearchRecommended.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun clickBackButton() {
        binding.clSearchTopBar.ivSearchBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeRecyclerViewItem() {
        lifecycleScope.launch {
            viewModel.recommendedKeyword?.collectLatest {
                recommendedAdapter.submitList(it)
            }
        }
    }

    private fun watchKeywordEditText() = with(binding.clSearchTopBar.etSearchSearchBar) {
        addTextChangedListener {
            viewModel.updateKeyword(it?.toString() ?: "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}