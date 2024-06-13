package com.stopsmoke.kekkek.presentation.popularWritingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPopularWritingListBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularWritingListFragment : Fragment() {
    private var _binding: FragmentPopularWritingListBinding? = null
    val binding: FragmentPopularWritingListBinding get() = _binding!!

    private val listAdapter: PopularWritingListAdapter by lazy {
        PopularWritingListAdapter()
    }

    private val viewModel: PopularWritingListViewModel by viewModels()

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
        initViewModel()
    }

    private fun initView() = with(binding) {
        initAppBar()
        rvPopularWritingList.adapter = listAdapter
        rvPopularWritingList.layoutManager = LinearLayoutManager(requireActivity())

        initListAdapterCallback()
    }


    private fun initListAdapterCallback() {
        listAdapter.registerCallbackListener(
            object : CommunityCallbackListener {
                override fun navigateToUserProfile(uid: String) {
                    findNavController().navigate(
                        resId = R.id.action_popularWritingList_to_userProfile,
                        args = bundleOf("uid" to uid)
                    )
                }

                override fun navigateToPost(communityWritingItem: CommunityWritingItem) {
                    findNavController().navigate(
                        resId = R.id.action_popularWritingList_to_postView,
                        args = bundleOf("item" to communityWritingItem)
                    )
                }
            }
        )
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
        listAdapter.unregisterCallbackListener()
        _binding = null
        activity?.visible()
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

}