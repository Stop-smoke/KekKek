package com.stopsmoke.kekkek.presentation.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.toPostCategory
import com.stopsmoke.kekkek.databinding.FragmentCommunityBinding
import com.stopsmoke.kekkek.databinding.ItemCommunityMainPopularBannerBinding
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
        setScrollToTop()
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

    private fun setScrollToTop() {
        binding.floatingActionButtonScrollToTop.setOnClickListener {
            binding.rvCommunityList.smoothScrollToPosition(0)
        }

        binding.rvCommunityList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastScrollY = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // dy > 0 : 스크롤을 아래로 내리는 경우
                // dy < 0 : 스크롤을 위로 올리는 경우
                if (dy > 0 && binding.floatingActionButtonScrollToTop.visibility == View.VISIBLE) {
                    binding.floatingActionButtonScrollToTop.hideWithAnimation()
                } else if (dy < 0 && binding.floatingActionButtonScrollToTop.visibility == View.GONE) {
                    binding.floatingActionButtonScrollToTop.showWithAnimation()
                }

                // 현재 스크롤 위치 저장
                lastScrollY = recyclerView.computeVerticalScrollOffset()
            }
        })    }

    private fun View.hideWithAnimation() {
        this.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction { this.visibility = View.GONE }
            .start()
    }

    private fun View.showWithAnimation() {
        this.visibility = View.VISIBLE
        this.alpha = 0f
        this.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
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
                        is CommunityUiState.ErrorExit -> errorExit(findNavController())
                    }
                }
        }

        //게시글
        posts.collectLatestWithLifecycle(lifecycle) { postsResult ->
            when(postsResult){
                is Result.Error -> {
                    postsResult.exception?.printStackTrace()
                    errorExit(findNavController())
                }
                Result.Loading -> {}
                is Result.Success ->  listAdapter.submitData(postsResult.data)
            }

        }

        //인기글 배너
        viewLifecycleOwner.lifecycleScope.launch {
            topPopularPosts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { popularPostsResult ->
                    when(popularPostsResult){
                        is Result.Error -> {
                            popularPostsResult.exception?.printStackTrace()
                            errorExit(findNavController())
                        }
                        Result.Loading -> {}
                        is Result.Success ->  bindPopularPosts(popularPostsResult.data, period = true)
                    }
                }
        }

        topPopularPostNonPeriod.collectLatestWithLifecycle(lifecycle){
            when(it){
                is Result.Error -> {
                    it.exception?.printStackTrace()
                    errorExit(findNavController())
                }
                Result.Loading -> {}
                is Result.Success ->  bindPopularPosts(it.data, period = false)
            }
        }

        //공지사항 배너
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noticeBanner.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { noticePostResult ->
                    when(noticePostResult){
                        is Result.Error -> {
                            noticePostResult.exception?.printStackTrace()
                            errorExit(findNavController())
                        }
                        Result.Loading -> {}
                        is Result.Success -> binding.tvCommunityNoticeTitle.text = noticePostResult.data.title
                    }
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

    private fun onBind(communityUiState: CommunityUiState.CommunityNormalUiState) = with(binding.includeCommunityPopularBanner){
        val popularItem = if(communityUiState.popularPeriod) communityUiState.popularItem else communityUiState.popularItemNonPeriod
        popularItem.postInfo1.postInfo.let {
            tvCommunityTitle1.text = it.title
            tvCommunityViewNum1.text = it.view.toString()
            tvCommunityLikeNum1.text = it.like.toString()
            tvCommunityCommentNum1.text = it.comment.toString()
            tvCommunityPostType1.text = it.postType
        }

        popularItem.postInfo2.postInfo.let {
            tvCommunityTitle2.text = it.title
            tvCommunityViewNum2.text = it.view.toString()
            tvCommunityLikeNum2.text = it.like.toString()
            tvCommunityCommentNum2.text = it.comment.toString()
            tvCommunityPostType2.text = it.postType
        }
    }
}