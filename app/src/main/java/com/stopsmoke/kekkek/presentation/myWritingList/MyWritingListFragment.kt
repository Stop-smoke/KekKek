package com.stopsmoke.kekkek.presentation.myWritingList

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
import com.stopsmoke.kekkek.databinding.FragmentMyWritingListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyWritingListFragment : Fragment() {
    private var _binding: FragmentMyWritingListBinding? = null
    val binding: FragmentMyWritingListBinding get() = _binding!!

    private val listAdapter: MyWritingListAdapter by lazy {
        MyWritingListAdapter()
    }

    private val viewModel: MyWritingLIstViewModel by lazy {
        ViewModelProvider(this)[MyWritingLIstViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyWritingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        initAppBar()
        rvMyWritingList.adapter = listAdapter
        rvMyWritingList.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun initAppBar() = with(binding) {
        val ivMyWritingListBack =
            requireActivity().findViewById<ImageView>(R.id.iv_notification_back)
        val ivMyWritingListDelete =
            requireActivity().findViewById<ImageView>(R.id.iv_notification_delete)
        val tvMyWritingListTitle =
            requireActivity().findViewById<TextView>(R.id.tv_notification_title)

        ivMyWritingListBack.setOnClickListener {
            findNavController().popBackStack()
        }

        ivMyWritingListDelete.visibility = View.GONE

        tvMyWritingListTitle.text = "내가 쓴 글"
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }
    }

    private fun onBind(uiState: MyWritingListUiState) = with(binding) {
        listAdapter.submitList(uiState.list)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MyWritingListFragment()
    }
}