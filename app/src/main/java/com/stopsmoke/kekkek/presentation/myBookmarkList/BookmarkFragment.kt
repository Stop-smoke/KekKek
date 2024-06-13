package com.stopsmoke.kekkek.presentation.myBookmarkList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentBookmarkBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.visible
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.visible
import com.stopsmoke.kekkek.presentation.post.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by viewModels()

    private val postViewModel by activityViewModels<PostViewModel>()

    private val listAdapter: BookmarkListAdapter by lazy {
        BookmarkListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postViewModel.updateMyBookmark()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        initObserveLiveData()
    }

    private fun initView() = with(binding) {
        initAppBar()
        initListAdapterCallback()
        rvBookmark.layoutManager = LinearLayoutManager(requireContext())
        rvBookmark.adapter = listAdapter

        viewModel.updateUserState()
    }

    private fun initListAdapterCallback() {
        listAdapter.registerCallbackListener(
            object : CommunityCallbackListener {
                override fun navigateToUserProfile(uid: String) {
                    findNavController().navigate(
                        resId = R.id.action_myBookmarkList_to_userProfile,
                        args = bundleOf("uid" to uid)
                    )
                }

                override fun navigateToPost(communityWritingItem: CommunityWritingItem) {
                    findNavController().navigate(
                        resId = R.id.action_myBookmarkList_to_postView,
                        args = bundleOf("item" to communityWritingItem)
                    )
                }
            }
        )
    }

    private fun initObserveLiveData() {
        postViewModel.bookmarkPosts.observe(viewLifecycleOwner) {
//            val bookmarkPosts = it.map { bookmark ->
//                CommunityWritingItem(
//                    userInfo = bookmark.userInfo,
//                    postInfo = bookmark.postInfo,
//                    postImage = bookmark.postImage,
//                    post = bookmark.post,
//                    postTime = bookmark.postTime,
//                    postType = bookmark.postType
//                )
//            }
//            val paging = PagingData.from(bookmarkPosts)
//            listAdapter.submitData(paging)
        }
    }

    private fun initAppBar() = with(binding) {
        val ivBookmarkBack = requireActivity().findViewById<ImageView>(R.id.iv_bookmark_back)
        ivBookmarkBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            myBookmarkPosts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { myBookmarkPosts ->
                    listAdapter.submitData(myBookmarkPosts)
                }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        listAdapter.unregisterCallbackListener()
        _binding = null
        activity?.visible()
    }
    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookmarkFragment()
    }
}