package com.stopsmoke.kekkek.presentation.rankingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.FragmentRankingListRegionalTopRankBinding

class RegionalTopRankFragment: Fragment() {
    private var _binding: FragmentRankingListRegionalTopRankBinding? = null
    private val binding: FragmentRankingListRegionalTopRankBinding get() = _binding!!

    private var regionData: String? = null

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