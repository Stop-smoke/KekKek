package com.stopsmoke.kekkek.presentation.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.stopsmoke.kekkek.PostWriteFragment
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentCommunityBinding
import com.stopsmoke.kekkek.presentation.my.MyViewModel
import com.stopsmoke.kekkek.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding: FragmentCommunityBinding get() = _binding!!

    private val viewModel: CommunityViewModel by lazy {
        ViewModelProvider(this)[CommunityViewModel::class.java]
    }

    private val viewPagerAutomaticFlippingJob by lazy {
        val viewPagerList = listOf(
            binding.vpCommunityPopularPost,
            binding.vpCommunityNotice
        )
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(3000)
                Log.d("coroutine", "dd")
                viewPagerList.forEach { viewPager ->
                    viewPager.adapter?.let { adapter ->
                        if (viewPager.currentItem + 1 < adapter.itemCount)
                            viewPager.currentItem += 1
                        else viewPager.currentItem = 0
                    }
                }

            }
        }
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
        setupListener()
    }

    override fun onResume() {
        super.onResume()
        activity?.visible()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewPagerAutomaticFlippingJob.cancel()
    }

    private fun initView() = with(binding) {
        vpCommunityPopularPost.adapter = CommunityPopularViewPagerAdapter(requireActivity())
        vpCommunityPopularPost.isUserInputEnabled = true
        TabLayoutMediator(tabLayoutCommunityPopularPost, vpCommunityPopularPost) { tab, position -> }.attach()

        vpCommunityNotice.adapter = CommunityNoticeViewPagerAdapter(requireActivity())
        vpCommunityNotice.isUserInputEnabled = true
        TabLayoutMediator(tabLayoutCommunityNotice, vpCommunityNotice){ tab, position -> }.attach()

        viewPagerAutomaticFlippingJob

        initSpinner()

    }

    private fun setupListener() = with(binding) {
        floatingActionButtonCommunity.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main, PostWriteFragment())
                addToBackStack(null)
            }
        }
    }

    private fun initViewModel() = with(viewModel){
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }
    }

    private fun onBind(communityUiState: CommunityUiState) {
        when (val category = communityUiState.communityCategory) { // home일 경우 viewPager에 넣을 아이템들
            is CommunityCategory.CommunityHome ->{
                val homeItems = category.popularItemList
                val noticeItems = category.noticeList
            }
            is CommunityCategory.CommunityList ->{ // home이 아닐 경우 출력할 글 목록들
                val writingList = category.list
            }
        }
    }

    private fun initSpinner() = with(binding){
        val category: Array<String> = resources.getStringArray(R.array.category)
        spnCommunityCategory.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.category, R.layout.item_spinner_custom)
        spnCommunityCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        // 커뮤니티 홈
                    }

                    1 -> {
                        // 공지사항
                    }

                    2 -> {
                        // 인기 게시판
                    }

                    3 -> {
                        // 자유 게시판
                    }

                    4 -> {
                        // 금연 성공 후기
                    }

                    5 -> {
                        // 금연 보조제 후기
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}