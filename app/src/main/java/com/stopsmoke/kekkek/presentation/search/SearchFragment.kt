package com.stopsmoke.kekkek.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentSearchBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.search.keyword.SearchKeywordFragment
import com.stopsmoke.kekkek.presentation.search.post.SearchPostFragment

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickBackButton()
        watchKeywordEditText()
        collectKeywordFragmentResult()
        collectSelectedRecommendSearchKeyword()
    }

    private fun clickBackButton() {
        binding.clSearchTopBar.ivSearchBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun collectKeywordFragmentResult() {
        val searchKeywordFragment = SearchKeywordFragment()
        val searchPostFragment = SearchPostFragment()

        viewModel.keyword.collectLatestWithLifecycle(lifecycle) {
            if (it.isBlank()) {
                childFragmentManager.commit {
                    replace(binding.fcvSearch.id, searchKeywordFragment)
                }
                return@collectLatestWithLifecycle
            }

            childFragmentManager.commit {
                replace(binding.fcvSearch.id, searchPostFragment)
            }
        }
    }

    private fun watchKeywordEditText() = with(binding.clSearchTopBar.etSearchSearchBar) {
        addTextChangedListener {
            viewModel.updateKeyword(it?.toString() ?: "")
        }
    }

    private fun collectSelectedRecommendSearchKeyword() {
        viewModel.selectedKeyword.collectLatestWithLifecycle(lifecycle) {
            binding.clSearchTopBar.etSearchSearchBar.setText(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}