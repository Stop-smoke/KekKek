package com.agvber.kekkek.presentation.my.comment

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
import com.agvber.kekkek.databinding.FragmentMyCommentBinding
import com.agvber.kekkek.presentation.collectLatestWithLifecycle
import com.agvber.kekkek.presentation.community.CommunityCallbackListener
import com.agvber.kekkek.presentation.error.ErrorHandle
import com.agvber.kekkek.presentation.invisible
import com.agvber.kekkek.presentation.isVisible
import com.agvber.kekkek.presentation.post.detail.navigateToPostDetailScreen
import com.agvber.kekkek.presentation.userprofile.navigateToUserProfileScreen
import com.agvber.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCommentFragment : Fragment(), ErrorHandle {

    private var _binding: FragmentMyCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyCommentViewModel by viewModels()

    private val listAdapter: MyCommentListAdapter by lazy {
        MyCommentListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        initAppBar()

        rvMyComment.adapter = listAdapter
        rvMyComment.layoutManager = LinearLayoutManager(requireContext())
        initListAdapterCallback()
    }

    private fun initListAdapterCallback() {
        listAdapter.registerCallbackListener(
            object : CommunityCallbackListener {
                override fun navigateToUserProfile(uid: String) {
                    findNavController().navigateToUserProfileScreen(uid)
                }
                override fun navigateToPost(postId: String) {
                    findNavController().navigateToPostDetailScreen(postId)
                }
            }
        )
    }

    private fun initAppBar() {
        val ivMyCommentBack =
            requireActivity().findViewById<ImageView>(R.id.iv_my_comment_back)

        ivMyCommentBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewModel() {
        viewModel.post.collectLatestWithLifecycle(lifecycle) {commentResult ->
            when(commentResult){
                is Result.Error -> {
                    commentResult.exception?.printStackTrace()
                    errorExit(findNavController())
                }
                Result.Loading -> {}
                is Result.Success -> listAdapter.submitData(commentResult.data)
            }
        }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        listAdapter.unregisterCallbackListener()
        activity?.visible()
    }
}