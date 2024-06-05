package com.stopsmoke.kekkek.presentation.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.DummyData
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentCommunityBinding
import com.stopsmoke.kekkek.presentation.post.PostWriteItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding: FragmentCommunityBinding get() = _binding!!

    private val viewModel: CommunityViewModel by lazy {
        ViewModelProvider(this)[CommunityViewModel::class.java]
    }

    private val listAdapter: CommunityListAdapter by lazy {
        CommunityListAdapter {
            findNavController().navigate("post_view")
        }
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
//        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun initView() = with(binding) {
        rvCommunityList.layoutManager = LinearLayoutManager(requireContext())
        rvCommunityList.adapter = listAdapter
        onBind(DummyData.CommunityList)
        ivCommunityNoticeArrow.setOnClickListener {
            // 인기글 전체보기 클릭
        }

        floatingActionButtonCommunity.setOnClickListener {
            findNavController().navigate("post_write")
        }

        rvCommunityCategory.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CommunityCategoryListAdapter()
        adapter.submitList(requireContext().resources.getStringArray(R.array.category).toList())
        rvCommunityCategory.adapter = adapter

        setToolbarMenu()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<PostWriteItem?>("NEW_POST")
            ?.observe(viewLifecycleOwner) { newPost ->
                Log.d("items",newPost.toString())
                // 성공해서 초기화 해야 될 때

                // 게시물 등록을 안 했을 때
            }
    }


    private fun setToolbarMenu() {
        binding.toolbarCommunity.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_search -> {
                    findNavController().navigate("search")
                }

                R.id.toolbar_my_bell -> {
                    findNavController().navigate("notification")
                }

                R.id.toolbar_community_setting -> {}
            }
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
    }

    private fun onBind(communityUiState: CommunityUiState) = with(binding) {
        when (communityUiState) {
            is CommunityUiState.CommunityNormalUiState -> {
                listAdapter.submitList(communityUiState.writingList)

                communityUiState.popularItem.postInfo1.let {
                    tvCommunityTitle1.text = it.title
                    tvCommunityViewNum1.text = it.view.toString()
                    tvCommunityLikeNum1.text = it.like.toString()
                    tvCommunityCommentNum1.text = it.comment.toString()
                    tvCommunityPostType1.text = it.postType
                }

                communityUiState.popularItem.postInfo2.let {
                    tvCommunityTitle2.text = it.title
                    tvCommunityViewNum2.text = it.view.toString()
                    tvCommunityLikeNum2.text = it.like.toString()
                    tvCommunityCommentNum2.text = it.comment.toString()
                    tvCommunityPostType2.text = it.postType
                }

                tvCommunityPopularFullView.setOnClickListener {
                    // 전체보기로 이동 추가
                }


            }
        }
    }
}