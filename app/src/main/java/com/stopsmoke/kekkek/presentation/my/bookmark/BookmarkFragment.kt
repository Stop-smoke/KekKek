package com.stopsmoke.kekkek.presentation.my.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentBookmarkBinding
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.isVisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.toCommunityWritingListItem
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    @Inject
    lateinit var userRepository: UserRepository

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by viewModels()

    private val listAdapter: BookmarkListAdapter by lazy {
        BookmarkListAdapter()
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
        initRecyclerView()
        updateRecyclerViewItem()
    }

    private fun initRecyclerView() = with(binding) {
        initAppBar()
        initListAdapterCallback()
        rvBookmark.layoutManager = LinearLayoutManager(requireContext())
        rvBookmark.adapter = listAdapter
    }

    private fun initListAdapterCallback() {
        listAdapter.registerCallbackListener(
            object : CommunityCallbackListener {
                override fun navigateToUserProfile(uid: String) {
                    findNavController().navigate(
                        resId = R.id.action_my_bookmark_screen_to_userProfile,
                        args = bundleOf("uid" to uid)
                    )
                }

                override fun navigateToPost(postId: String) {
                    findNavController().navigate(
                        resId = R.id.action_my_bookmark_screen_to_post_view,
                        args = bundleOf("post_id" to postId)
                    )
                }
            }
        )
    }

    private fun initAppBar() = with(binding) {
        val ivBookmarkBack = requireActivity().findViewById<ImageView>(R.id.iv_bookmark_back)
        ivBookmarkBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateRecyclerViewItem() {
        viewModel.post.collectLatestWithLifecycle(lifecycle) {
            listAdapter.submitData(it.map { pagingData -> pagingData.toCommunityWritingListItem() })
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
        activity?.let { activity ->
            if (activity.isVisible()) {
                listAdapter.refresh()
            }
            activity.invisible()
        }
    }
}