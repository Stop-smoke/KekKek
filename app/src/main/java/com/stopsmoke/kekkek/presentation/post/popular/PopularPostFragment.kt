package com.stopsmoke.kekkek.presentation.post.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPopularPostBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityUiState
import com.stopsmoke.kekkek.presentation.community.CommunityViewModel
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.post.detail.navigateToPostDetailScreen
import com.stopsmoke.kekkek.presentation.userprofile.navigateToUserProfileScreen
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularPostFragment : Fragment(),ErrorHandle {
    private var _binding: FragmentPopularPostBinding? = null
    val binding: FragmentPopularPostBinding get() = _binding!!

    private val listAdapter: PopularPostListAdapter by lazy {
        PopularPostListAdapter()
    }

    private val  viewModel: CommunityViewModel by activityViewModels()

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
        communityUiState.collectLatestWithLifecycle(lifecycle){ state ->
            when(state) {
                is CommunityUiState.CommunityNormalUiState -> onBind(state)
                is CommunityUiState.ErrorExit -> errorExit(findNavController())
            }
        }
    }

    private fun onBind(communityUiState: CommunityUiState.CommunityNormalUiState) {
        val popularItem = if(communityUiState.popularPeriod) communityUiState.popularItem else communityUiState.popularItemNonPeriod

        listAdapter.submitList(popularItem)
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