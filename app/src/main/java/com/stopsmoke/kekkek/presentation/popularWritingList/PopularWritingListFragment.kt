package com.stopsmoke.kekkek.presentation.popularWritingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPopularWritingListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PopularWritingListFragment : Fragment() {
    private var _binding: FragmentPopularWritingListBinding? = null
    val binding: FragmentPopularWritingListBinding get() = _binding!!

    private val listAdapter: PopularWritingListAdapter by lazy {
        PopularWritingListAdapter()
    }

    private val viewModel: PopularWritingListViewModel by lazy {
        ViewModelProvider(this)[PopularWritingListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularWritingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        initAppBar()
        rvPopularWritingList.adapter = listAdapter
        rvPopularWritingList.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun initAppBar() = with(binding) {
        val ivPopularWritingListBack =
            requireActivity().findViewById<ImageView>(R.id.iv_popularWritingLstAppBar_back)
        val tvPopularWritingListTitle =
            requireActivity().findViewById<TextView>(R.id.tv_popularWritingLstAppBar_title)

        ivPopularWritingListBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tvPopularWritingListTitle.text = "인기 글"
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }
    }

    private fun onBind(uiState: PopularWritingListUiState) = with(binding) {
        listAdapter.submitList(uiState.list)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PopularWritingListFragment()
    }
}