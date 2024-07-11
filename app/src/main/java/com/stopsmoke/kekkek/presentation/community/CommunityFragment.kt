package com.stopsmoke.kekkek.presentation.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.domain.model.toPostCategory
import com.stopsmoke.kekkek.databinding.FragmentCommunityBinding
import com.stopsmoke.kekkek.presentation.NavigationKey
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.notification.navigateToNotificationScreen
import com.stopsmoke.kekkek.presentation.post.detail.navigateToPostDetailScreen
import com.stopsmoke.kekkek.presentation.post.edit.navigateToPostEditScreen
import com.stopsmoke.kekkek.presentation.post.notice.navigateToPostNoticeScreen
import com.stopsmoke.kekkek.presentation.post.popular.navigateToPostPopularScreen
import com.stopsmoke.kekkek.presentation.search.navigateToSearchScreen
import com.stopsmoke.kekkek.presentation.settings.navigateToSettingsGraph
import com.stopsmoke.kekkek.presentation.userprofile.navigateToUserProfileScreen
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CommunityFragment : Fragment(), ErrorHandle {
    private var _binding: FragmentCommunityBinding? = null
    private val binding: FragmentCommunityBinding get() = _binding!!

    private val viewModel: CommunityViewModel by viewModels()

    private val listAdapter: CommunityListAdapter by lazy {
        CommunityListAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        activity?.visible()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        listAdapter.unregisterCallbackListener()
    }

    private fun initView() {
        initRecyclerView()
        initCommunityCategory()
        setToolbarMenu()
        resetCommunityListScrollOnUpdate()
    }

    private fun initRecyclerView() {
        binding.rvCommunityList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCommunityList.adapter = listAdapter
    }

    private fun setClickListener() = with(binding) {
        floatingActionButtonCommunity.setOnClickListener {
            findNavController().navigateToPostEditScreen()
        }

        clCommunityNotice.setOnClickListener {
            findNavController().navigateToPostNoticeScreen()
        }


        //인기글 배너 클릭 리스너
        val tvCommunityPopularFullView =
            requireActivity().findViewById<TextView>(R.id.tv_community_popularFullView)
        val clCommunityPostPopular1 =
            requireActivity().findViewById<ConstraintLayout>(R.id.cl_community_postPopular1)
        val clCommunityPostPopular2 =
            requireActivity().findViewById<ConstraintLayout>(R.id.cl_community_postPopular2)

        tvCommunityPopularFullView.setOnClickListener {
            findNavController().navigateToPostPopularScreen()
        }

        clCommunityPostPopular1.setOnClickListener {
            val item = viewModel.uiState.value
            if (item is CommunityUiState.CommunityNormalUiState) {
                findNavController().navigateToPostDetailScreen(item.popularItem.postInfo1.postInfo.id)
            }
        }
        clCommunityPostPopular2.setOnClickListener {
            val item = viewModel.uiState.value
            if (item is CommunityUiState.CommunityNormalUiState) {
                findNavController().navigateToPostDetailScreen(item.popularItem.postInfo2.postInfo.id)
            }
        }

        //게시글 클릭
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

    private fun initCommunityCategory() = with(binding) {
        rvCommunityCategory.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterList =
            requireContext().resources.getStringArray(R.array.community_category).toList()
        val adapter = CommunityCategoryListAdapter { clickPosition ->
            if (viewModel.category.value != adapterList[clickPosition].toPostCategory()) {
                viewModel.setCategory(adapterList[clickPosition])
                return@CommunityCategoryListAdapter
            }
            binding.rvCommunityList.smoothScrollToPosition(0)
            binding.rvCommunityCategory.smoothScrollToPosition(0)
        }
        adapter.submitList(adapterList)
        rvCommunityCategory.adapter = adapter
    }

    private fun setToolbarMenu() {
        with(binding.includeCommunityAppBar) {
            icCommunityBell.setOnClickListener {
                findNavController().navigateToNotificationScreen()
            }
            icCommunitySettings.setOnClickListener {
                findNavController().navigateToSettingsGraph()
            }
            icCommunitySearch.setOnClickListener {
                findNavController().navigateToSearchScreen()
            }
        }
    }

    private fun initViewModel() = with(viewModel) {

        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    when(state) {
                        is CommunityUiState.CommunityNormalUiState -> onBind(state)
                        CommunityUiState.ErrorExit -> errorExit(findNavController())
                    }
                }
        }

        //게시글
        posts.collectLatestWithLifecycle(lifecycle) { posts ->
            listAdapter.submitData(posts)
        }

        //인기글 배너
        viewLifecycleOwner.lifecycleScope.launch {
            topPopularPosts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { popularPosts ->
                    bindPopularPosts(popularPosts)
                }
        }

        //공지사항 배너
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noticeBanner.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { noticePost ->
                    binding.tvCommunityNoticeTitle.text = noticePost.title
                }
        }

        // 삭제될 때 시도
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        val isPostDeletedFlow = savedStateHandle?.getStateFlow(NavigationKey.IS_DELETED_POST, false)

        isPostDeletedFlow?.collectLatestWithLifecycle(lifecycle) { isDeleted ->
            if (isDeleted) {
                binding.rvCommunityList.smoothScrollToPosition(0)
                listAdapter.refresh()
            }
        }
    }

    private fun resetCommunityListScrollOnUpdate() {
        listAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    super.onItemRangeChanged(positionStart, itemCount, payload)
                    binding.rvCommunityList.scrollToPosition(0)
                }
            }
        )
    }

    private fun onBind(communityUiState: CommunityUiState.CommunityNormalUiState) {
        val tvCommunityTitle1 =
            requireActivity().findViewById<TextView>(R.id.tv_community_title1)
        val tvCommunityViewNum1 =
            requireActivity().findViewById<TextView>(R.id.tv_community_viewNum1)
        val tvCommunityLikeNum1 =
            requireActivity().findViewById<TextView>(R.id.tv_community_likeNum1)
        val tvCommunityCommentNum1 =
            requireActivity().findViewById<TextView>(R.id.tv_community_commentNum1)
        val tvCommunityPostType1 =
            requireActivity().findViewById<TextView>(R.id.tv_community_postType1)

        val tvCommunityTitle2 =
            requireActivity().findViewById<TextView>(R.id.tv_community_title2)
        val tvCommunityViewNum2 =
            requireActivity().findViewById<TextView>(R.id.tv_community_viewNum2)
        val tvCommunityLikeNum2 =
            requireActivity().findViewById<TextView>(R.id.tv_community_likeNum2)
        val tvCommunityCommentNum2 =
            requireActivity().findViewById<TextView>(R.id.tv_community_commentNum2)
        val tvCommunityPostType2 =
            requireActivity().findViewById<TextView>(R.id.tv_community_postType2)

        communityUiState.popularItem.postInfo1.postInfo.let {
            tvCommunityTitle1.text = it.title
            tvCommunityViewNum1.text = it.view.toString()
            tvCommunityLikeNum1.text = it.like.toString()
            tvCommunityCommentNum1.text = it.comment.toString()
            tvCommunityPostType1.text = it.postType
        }

        communityUiState.popularItem.postInfo2.postInfo.let {
            tvCommunityTitle2.text = it.title
            tvCommunityViewNum2.text = it.view.toString()
            tvCommunityLikeNum2.text = it.like.toString()
            tvCommunityCommentNum2.text = it.comment.toString()
            tvCommunityPostType2.text = it.postType
        }
    }
}