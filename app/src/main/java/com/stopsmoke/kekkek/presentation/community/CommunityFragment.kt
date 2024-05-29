package com.stopsmoke.kekkek.presentation.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentCommunityBinding
import com.stopsmoke.kekkek.presentation.my.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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


    private fun initSpinner() = with(binding){
        val category: Array<String> = resources.getStringArray(R.array.category)
        spnCommunityCategory.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.category, android.R.layout.simple_spinner_item)
        spnCommunityCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        tvCommunitySelectCategory.text = category[0]
                    }

                    1 -> {
                        tvCommunitySelectCategory.text = category[1]
                    }

                    2 -> {
                        tvCommunitySelectCategory.text = category[2]
                    }

                    3 -> {
                        tvCommunitySelectCategory.text = category[3]
                    }

                    4 -> {
                        tvCommunitySelectCategory.text = category[4]
                    }

                    5 -> {
                        tvCommunitySelectCategory.text = category[5]
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}