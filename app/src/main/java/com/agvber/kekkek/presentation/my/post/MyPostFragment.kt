package com.agvber.kekkek.presentation.my.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agvber.kekkek.R
import com.agvber.kekkek.common.Result
import com.agvber.kekkek.databinding.FragmentMyPostBinding
import com.agvber.kekkek.presentation.collectLatestWithLifecycle
import com.agvber.kekkek.presentation.community.CommunityCallbackListener
import com.agvber.kekkek.presentation.error.ErrorHandle
import com.agvber.kekkek.presentation.invisible
import com.agvber.kekkek.presentation.isVisible
import com.agvber.kekkek.presentation.post.detail.navigateToPostDetailScreen
import com.agvber.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostFragment : Fragment(), ErrorHandle {
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