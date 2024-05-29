package com.stopsmoke.kekkek.presentation.community


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.ItemCommunityPopularhomeBinding

class PopularHomeItemFragment: Fragment() {
    private var _binding: ItemCommunityPopularhomeBinding? = null

    private val binding: ItemCommunityPopularhomeBinding get() = _binding!!
    private var item: CommunityPopularItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getSerializable("CommunityPopularItem") as CommunityPopularItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemCommunityPopularhomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageResource = view.resources.getIdentifier(item?.imageResource,"drawable", view.context.packageName)
        binding.ivItemPopularImage.setImageResource(imageResource)

    }

    private fun initView() = with(binding){
        tvItemPopularTitle.text = item?.title
    }

    companion object {
        @JvmStatic
        fun newInstance(item: CommunityPopularItem) =
            PopularHomeItemFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("CommunityPopularItem", item)
                }
            }
    }

}