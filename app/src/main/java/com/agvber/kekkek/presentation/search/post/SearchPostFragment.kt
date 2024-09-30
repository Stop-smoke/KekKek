package com.agvber.kekkek.presentation.search.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agvber.kekkek.common.Result
import com.agvber.kekkek.databinding.FragmentSearchPostBinding
import com.agvber.kekkek.presentation.collectLatestWithLifecycle
import com.agvber.kekkek.presentation.error.ErrorHandle
import com.agvber.kekkek.presentation.post.detail.navigateToPostDetailScreen
import com.agvber.kekkek.presentation.search.SearchViewModel

class SearchPostFragment : Fragment(), ErrorHandle {

    private var _binding: FragmentSearchPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    private lateinit var postSearchAdapter: SearchPostRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observePostRecyclerViewItem()
    }

    private fun initRecyclerView() {
        postSearchAdapter = SearchPostRecyclerViewAdapter {
            findNavController().navigateToPostDetailScreen(it.id)
        }
        binding.root.adapter = postSearchAdapter
        binding.root.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observePostRecyclerViewItem() {
        viewModel.post.collectLatestWithLifecycle(lifecycle) {
            when(it){
                is Result.Error ->  {
                    it.exception?.printStackTrace()
                    errorExit(findNavController())
                }
                Result.Loading -> {}
                is Result.Success ->  postSearchAdapter.submitData(it.data)
            }
        }
    }
}