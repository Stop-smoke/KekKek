package com.stopsmoke.kekkek.presentation.rankingMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentRankingMapBinding

class RankingMapFragment : Fragment() {
    private var _binding: FragmentRankingMapBinding? = null
    private val binding: FragmentRankingMapBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRankingMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setAppbar() {
        val ivMyWritingMapBack =
            requireActivity().findViewById<ImageView>(R.id.iv_rankingList_back)
        val tvMyWritingMapTitle =
            requireActivity().findViewById<TextView>(R.id.tv_rankingList_title)

        ivMyWritingMapBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tvMyWritingMapTitle.text = "내 지역 설정"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RankingMapFragment()
    }
}