package com.stopsmoke.kekkek.presentation.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.stopsmoke.kekkek.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding: FragmentCommunityBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding){
        vpCommunityPopularPost.adapter = CommunityViewPagerAdapter(requireActivity())
        vpCommunityPopularPost.isUserInputEnabled = true

        TabLayoutMediator(tabLayoutCommunityPopularPost, vpCommunityPopularPost) { tab, position ->
        }.attach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CommunityFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}