package com.stopsmoke.kekkek.presentation.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.DummyData
import com.stopsmoke.kekkek.databinding.FragmentBookmarkBinding
import com.stopsmoke.kekkek.presentation.community.CommunityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by lazy {
        ViewModelProvider(this)[BookmarkViewModel::class.java]
    }

    private val listAdapter: BookmarkListAdapter by lazy{
        BookmarkListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
//        initViewModel()
    }
    
    private fun initView() = with(binding){
        initAppBar()
        rvBookmark.layoutManager = LinearLayoutManager(requireContext())
        rvBookmark.adapter = listAdapter
        listAdapter.submitList(DummyData.bookmarkList)
    }

    private fun initAppBar() = with(binding) {
        clBookmarkAppBar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }
    }

    private fun onBind(uiState: BookmarkUiState) = with(binding){
        listAdapter.submitList(uiState.bookmarkList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookmarkFragment()
    }
}