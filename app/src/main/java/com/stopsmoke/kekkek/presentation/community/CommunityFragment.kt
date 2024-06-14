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
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentCommunityBinding
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

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(requireContext(), GestureListener())
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
        initGestureDetector()


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

        // 삭제될 때 시도
        viewModel.isPostDeleted.collectLatestWithLifecycle(lifecycle){ isDeleted ->
            if (isDeleted) {
                listAdapter.refresh()
                viewModel.setPostDeleted(false)
            }
        }


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


    private fun initView() = with(binding) {
        rvCommunityList.layoutManager = LinearLayoutManager(requireContext())
        rvCommunityList.adapter = listAdapter

        ivCommunityNoticeArrow.setOnClickListener {
            // 인기글 전체보기 클릭
        }

        floatingActionButtonCommunity.setOnClickListener {
            findNavController().navigate("post_write")
        }

        initCommunityCategory()
        setToolbarMenu()

        clCommunityNotice.setOnClickListener {
            findNavController().navigate("notice_list")
        }


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
    }

    private fun initCommunityCategory() = with(binding) {
        rvCommunityCategory.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterList =
            requireContext().resources.getStringArray(R.array.community_category).toList()
        val adapter = CommunityCategoryListAdapter(onClick = { clickPosition ->
            viewModel.setCategory(adapterList[clickPosition])
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

    private fun initGestureDetector() {
        binding.coordinatorLayoutCommunityParent.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            posts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { posts ->
                    listAdapter.submitData(posts)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noticeBanner.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { noticePost ->
                    binding.tvCommunityNoticeTitle.text = noticePost.title
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

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null || e2 == null) return false

            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (Math.abs(diffY) > Math.abs(diffX)) {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY < 0) {
                        onSwipeUp()
                    }
                }
            }
            return true
        }
    }

    private fun onSwipeUp() {
        viewModel.setCategory("")
    }
}