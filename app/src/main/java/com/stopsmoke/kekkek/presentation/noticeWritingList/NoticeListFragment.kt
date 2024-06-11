package com.stopsmoke.kekkek.presentation.noticeWritingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentNoticeListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NoticeListFragment : Fragment() {
    private var _binding: FragmentNoticeListBinding? = null
    val binding: FragmentNoticeListBinding get() = _binding!!

    private val listAdapter: NoticeListAdapter by lazy {
        NoticeListAdapter()
    }

    private val viewModel: NoticeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        initAppBar()
        rvNoticeList.adapter = listAdapter
        rvNoticeList.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun initAppBar() = with(binding) {
        val ivNoticeListBack =
            requireActivity().findViewById<ImageView>(R.id.iv_notification_back)
        val ivNoticeListDelete =
            requireActivity().findViewById<ImageView>(R.id.iv_notification_delete)
        val tvNoticeListTitle =
            requireActivity().findViewById<TextView>(R.id.tv_notification_title)

        ivNoticeListBack.setOnClickListener {
            findNavController().popBackStack()
        }

        ivNoticeListDelete.visibility = View.GONE

        tvNoticeListTitle.text = "공지사항"
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            noticePosts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { noticePosts ->
                    listAdapter.submitData(noticePosts)
                }
        }
    }

    private fun onBind(uiState: NoticeListUiState) = with(binding) {
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoticeListFragment()
    }
}