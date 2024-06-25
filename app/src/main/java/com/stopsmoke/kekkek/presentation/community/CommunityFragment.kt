package com.stopsmoke.kekkek.presentation.community

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentCommunityBinding
import com.stopsmoke.kekkek.domain.model.toPostCategory
import com.stopsmoke.kekkek.isVisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding: FragmentCommunityBinding get() = _binding!!

    private val viewModel: CommunityViewModel by activityViewModels()

    private val listAdapter: CommunityListAdapter by lazy {
        CommunityListAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        activity?.let { activity ->
            activity.visible()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        listAdapter.unregisterCallbackListener()
    }


    private fun initView() = with(binding) {
        initRecyclerView()
        initCommunityCategory()
        setToolbarMenu()

    }

    private fun initRecyclerView(){
        binding.rvCommunityList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCommunityList.adapter = listAdapter
    }

    private fun setClickListener() = with(binding){
        floatingActionButtonCommunity.setOnClickListener {
            findNavController().navigate("post_write")
        }

        clCommunityNotice.setOnClickListener {
            findNavController().navigate("notice_list")
        }


        //인기글 배너 클릭 리스너
        val tvCommunityPopularFullView =
            requireActivity().findViewById<TextView>(R.id.tv_community_popularFullView)
        val clCommunityPostPopular1 =
            requireActivity().findViewById<ConstraintLayout>(R.id.cl_community_postPopular1)
        val clCommunityPostPopular2 =
            requireActivity().findViewById<ConstraintLayout>(R.id.cl_community_postPopular2)

        tvCommunityPopularFullView.setOnClickListener {
            findNavController().navigate(R.id.action_community_to_popularWritingList)
        }

        clCommunityPostPopular1.setOnClickListener {
            val item = viewModel.uiState.value
            if (item is CommunityUiState.CommunityNormalUiState) {
                findNavController().navigate(
                    resId = R.id.action_community_to_post_view,
                    args = bundleOf("post_id" to item.popularItem.postInfo1.postInfo.id)
                )
            }
        }
        clCommunityPostPopular2.setOnClickListener {
            val item = viewModel.uiState.value
            if (item is CommunityUiState.CommunityNormalUiState) {
                findNavController().navigate(
                    resId = R.id.action_community_to_post_view,
                    args = bundleOf("post_id" to item.popularItem.postInfo2.postInfo.id)
                )
            }
        }

        //게시글 클릭
        listAdapter.registerCallbackListener(
            object : CommunityCallbackListener {
                override fun navigateToUserProfile(uid: String) {

                    findNavController().navigate(
                        resId = R.id.action_community_to_user_profile_screen,
                        args = bundleOf("uid" to uid)
                    )
                }

                override fun navigateToPost(postId: String) {
                    findNavController().navigate(
                        resId = R.id.action_community_to_post_view,
                        args = bundleOf("post_id" to postId)
                    )
                }
            }
        )
    }

    private fun initCommunityCategory() = with(binding) {
        rvCommunityCategory.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterList =
            requireContext().resources.getStringArray(R.array.community_category).toList()
        val adapter = CommunityCategoryListAdapter(onClick = { clickPosition ->
            if (viewModel.category.value == adapterList[clickPosition].toPostCategory()) {
                listAdapter.refresh()
            } else {
                viewModel.setCategory(adapterList[clickPosition])
            }

            rvCommunityList.scrollToPosition(0)
        })
        adapter.submitList(adapterList)
        rvCommunityCategory.adapter = adapter
    }

    private fun setToolbarMenu() {
        with(binding.includeCommunityAppBar) {
            icCommunityBell.setOnClickListener {
                findNavController().navigate("notification")
            }
            icCommunitySettings.setOnClickListener {
                findNavController().navigate(R.id.action_community_page_to_nav_settings)
            }
        }
    }



    private fun initViewModel() = with(viewModel) {

        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }

        //게시글
        viewLifecycleOwner.lifecycleScope.launch {
            posts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { posts ->
                    listAdapter.submitData(posts)
                }
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
        viewModel.isPostChanged.collectLatestWithLifecycle(lifecycle) { isDeleted ->
            if (isDeleted) {
                listAdapter.refresh()
                viewModel.setPostChanged(false)
            }
        }
    }

    private fun onBind(communityUiState: CommunityUiState) {
        when (communityUiState) {
            is CommunityUiState.CommunityNormalUiState -> {
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
    }
}