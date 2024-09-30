package com.agvber.kekkek.presentation.search.keyword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.databinding.FragmentSearchKeywordBinding
import com.agvber.kekkek.presentation.collectLatestWithLifecycle
import com.agvber.kekkek.presentation.search.SearchViewModel
import com.agvber.kekkek.presentation.snackbarLongShow

class SearchKeywordFragment : Fragment() {

    private var _binding: FragmentSearchKeywordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    private lateinit var recommendedAdapter: KeywordRecommendedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchKeywordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUser()
        initRecyclerView()
        observeRecyclerViewItem()
    }

    private fun initUser() = viewModel.user.collectLatestWithLifecycle(lifecycle) {
        when (it) {
            is User.Error -> {
                view?.snackbarLongShow("user 에러")
            }

            is User.Guest -> {
                binding.tvSearchRecommend.text = getString(R.string.search_recommend_keyword)
            }

            is User.Registered -> {
                binding.tvSearchRecommend.text = it.name + getString(R.string.search_recommend_keyword)
            }
        }
    }

    private fun initRecyclerView() {
        recommendedAdapter = KeywordRecommendedListAdapter {
            viewModel.updateSelectedKeyword(it)
        }
        binding.rvSearchRecommended.adapter = recommendedAdapter
        binding.rvSearchRecommended.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeRecyclerViewItem() {
        viewModel.recommendedKeyword.collectLatestWithLifecycle(lifecycle) {
            recommendedAdapter.submitList(it)
        }
    }
}