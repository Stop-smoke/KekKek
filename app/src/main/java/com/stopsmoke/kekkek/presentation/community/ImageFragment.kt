package com.stopsmoke.kekkek.presentation.community


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.ItemImageBinding

private const val IMAGE_RESOURCE = "ImageResource"

class ImageFragment: Fragment() {
    private var _binding: ItemImageBinding? = null
    private val binding: ItemImageBinding get() = _binding!!
    private var imageResource: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageResource = it.getString(IMAGE_RESOURCE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //나중에 url로 glide, coil로 바꾸기
        val imageResourceInit = view.resources.getIdentifier(imageResource,"drawable", view.context.packageName)
        binding.ivItemImage.setImageResource(imageResourceInit)
    }

    companion object {
        @JvmStatic
        fun newInstance(imageResource: String) =
            CommunityFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_RESOURCE, imageResource)
                }
            }
    }

}