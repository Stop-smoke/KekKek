package com.stopsmoke.kekkek.presentation.community


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.stopsmoke.kekkek.PostViewFragment
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemCommunityPostpopularBinding

class PopularHomeItemFragment: Fragment() {

    private var _binding: ItemCommunityPostpopularBinding? = null
    private val binding: ItemCommunityPostpopularBinding get() = _binding!!

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
        _binding = ItemCommunityPostpopularBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val imageResource = view.resources.getIdentifier(item?.imageResource,"drawable", view.context.packageName)
//        binding.ivItemPopularImage.setImageResource(imageResource)
//
//        initListener()
//    }
//
//    private fun initView() = with(binding) {
//        tvItemPopularTitle.text = item?.title
//    }
//
//    private fun initListener() = with(binding) {
//        ivItemPopularImage.setOnClickListener {
//            parentFragmentManager.commit {
//                replace(R.id.main,PostViewFragment())
//                addToBackStack(null)
//            }
//        }
//    }

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