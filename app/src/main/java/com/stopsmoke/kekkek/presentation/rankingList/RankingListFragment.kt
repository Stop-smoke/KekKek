package com.stopsmoke.kekkek.presentation.rankingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentRankingListBinding

class RankingListFragment : Fragment() {
    private var _binding: FragmentRankingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RankingListViewModel by viewModels()

    private val listAdapter: RankingListAdapter by lazy {
        RankingListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRankingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        initViewPager()
        setBackPressed()
        initRankRecyclerview()

    }

    private fun setBackPressed(){
        val ivRankingListBack =
            requireActivity().findViewById<ImageView>(R.id.iv_rankingList_back)
        ivRankingListBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initRankRecyclerview(){
        binding.rvRankingListRankState.adapter = listAdapter
        binding.rvRankingListRankState.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun initViewPager() = with(binding) {
        vpRankingListRegionalTopRank.adapter = RankingListViewpagerAdapter(this@RankingListFragment)
        vpRankingListRegionalTopRank.isUserInputEnabled = false

        val tabTitles = listOf("지역", "전국", "업적")

        TabLayoutMediator(tabLayoutRankingListRegionalUnit, vpRankingListRegionalTopRank) { tab, position ->
            val customView = LayoutInflater.from(context).inflate(R.layout.custom_tab_ranking_list, null)
            val textView = customView as TextView
            textView.text = tabTitles[position]
            tab.customView = customView
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
