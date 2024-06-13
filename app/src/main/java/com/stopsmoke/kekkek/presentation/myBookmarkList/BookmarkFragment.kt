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
import com.google.firebase.firestore.FirebaseFirestore
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentBookmarkBinding
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.post.PostViewModel
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    @Inject
    lateinit var userRepository: UserRepository

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
        postViewModel.bookmarkPosts.observe(viewLifecycleOwner) { bookmarkList ->
            lifecycleScope.launch {
                val user = userRepository.getUserData().first()
                when (user) {
                    is User.Error -> {} // 에러 핸들링
                    User.Guest -> { } // 에러 핸들링
                    is User.Registered -> {
                        val db = FirebaseFirestore.getInstance()
                        for (i in bookmarkList.indices) {
                            db.collection("user")
                                .document(user.uid)
                                .update("post_bookmark", bookmarkList[i].postInfo.id)
                        }
                    }
                }
            }
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