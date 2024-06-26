package com.stopsmoke.kekkek.presentation.rankingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.stopsmoke.kekkek.databinding.FragmentRankingListRegionalTopRankBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import java.time.Duration
import java.time.LocalDateTime

class RegionalTopRankFragment: Fragment() {
    private var _binding: FragmentRankingListRegionalTopRankBinding? = null
    private val binding: FragmentRankingListRegionalTopRankBinding get() = _binding!!

    private var regionData: String? = null
    private var rankingData: List<RankingListItem>? = null

    private val viewModel: RankingListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            regionData = it.getString(REGIONAL_DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRankingListRegionalTopRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.topUserList.collectLatestWithLifecycle(lifecycle){
            bind(it)
        }
    }

    fun bind(list : List<RankingListItem>) = with(binding){
        val nameList = listOf(
            tvTopRankNumOneName,
            tvTopRankNumTwoName,
            tvTopRankNumThreeName
        )
        val timeList = listOf(
            tvTopRankNumOneTime,
            tvTopRankNumTwoTime,
            tvTopRankNumThreeTime
        )

        list.forEachIndexed() { index, item ->
            when(index){
                0 -> {
                    nameList[0].text = item.name
                    timeList[0].text = getRankingTime(item.startTime!!)
                }

                1 -> {
                    nameList[1].text = item.name
                    timeList[1].text = getRankingTime(item.startTime!!)
                }

                2 -> {
                    nameList[2].text = item.name
                    timeList[2].text = getRankingTime(item.startTime!!)
                }

                else ->{}
            }
        }
    }

    private fun getRankingTime(startTime: LocalDateTime): String { //-일 -시간
        val currentTime = LocalDateTime.now()
        val duration = Duration.between(startTime, currentTime)

        val days = duration.toDays()
        val hours = duration.toHours() % 24


        return "${days}일 ${hours}시간"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        private const val REGIONAL_DATA = "regional_data"
        fun newInstance(region: String) = RegionalTopRankFragment().apply{
            arguments = Bundle().apply {
                putString(REGIONAL_DATA, region)
            }
        }
    }
}