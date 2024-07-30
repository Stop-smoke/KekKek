package com.stopsmoke.kekkek.presentation.my.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.databinding.FragmentMyPostBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.error.errorExit
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.isVisible
import com.stopsmoke.kekkek.presentation.post.detail.navigateToPostDetailScreen
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostFragment : Fragment() {
    private var _binding: FragmentMyPostBinding? = null
    private val binding: FragmentMyPostBinding get() = _binding!!

    private val listAdapter: MyPostListAdapter by lazy {
        MyPostListAdapter()
    }

    private val viewModel: MyPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewModel.post.collectLatestWithLifecycle(lifecycle) {postResult ->
            when(postResult){
                is Result.Error -> {
                    postResult.exception?.printStackTrace()
                    errorExit(findNavController())
                }
                Result.Loading -> {
                }
                is Result.Success -> {
                    listAdapter.submitData(postResult.data)
                }
            }
        }
    }

    private fun initView() = with(binding) {
        initAppBar()
        rvMyWritingList.adapter = listAdapter
        rvMyWritingList.layoutManager = LinearLayoutManager(requireActivity())

        initListAdapterCallback()
    }

    private fun initListAdapterCallback() {
        listAdapter.registerCallbackListener(
            object : CommunityCallbackListener {
                override fun navigateToUserProfile(uid: String) {

                }

                override fun navigateToPost(postId: String) {
                    findNavController().navigateToPostDetailScreen(postId)
                }
            }
        )
    }

    private fun initAppBar() {
        val ivMyPostBack =
            requireActivity().findViewById<ImageView>(R.id.iv_my_post_back)

        ivMyPostBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        activity?.visible()
        listAdapter.unregisterCallbackListener()
    }

    override fun onResume() {
        super.onResume()
        activity?.let { activity ->
            if (activity.isVisible()) {
                listAdapter.refresh()
            }
            activity.invisible()
        }
    }

}