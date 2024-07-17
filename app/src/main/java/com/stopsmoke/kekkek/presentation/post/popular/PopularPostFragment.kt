package com.stopsmoke.kekkek.presentation.post.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.databinding.FragmentPopularPostBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.toCommunityWritingListItem
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.isVisible
import com.stopsmoke.kekkek.presentation.post.detail.navigateToPostDetailScreen
import com.stopsmoke.kekkek.presentation.userprofile.navigateToUserProfileScreen
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularPostFragment : Fragment(),ErrorHandle {
    private var _binding: FragmentPopularPostBinding? = null
    val binding: FragmentPopularPostBinding get() = _binding!!

    private val listAdapter: PopularPostListAdapter by lazy {
        PopularPostListAdapter()
    }

    private val viewModel: PopularPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularPostBinding.inflate(inflater, container, false)
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
                    findNavController().navigateToUserProfileScreen(uid)
                }

                override fun navigateToPost(postId: String) {
                    findNavController().navigateToPostDetailScreen(postId)
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
            post.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { postResult ->
                    when(postResult){
                        is Result.Error -> {
                            postResult.exception?.printStackTrace()
                            errorExit(findNavController())
                        }
                        Result.Loading -> {}
                        is Result.Success -> listAdapter.submitList(postResult.data.map{it.toCommunityWritingListItem()})
                    }
                }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        listAdapter.unregisterCallbackListener()
        _binding = null
        activity?.visible()
    }

    override fun onResume() {
        super.onResume()
        activity?.let { activity ->
            activity.invisible()
        }
    }

}