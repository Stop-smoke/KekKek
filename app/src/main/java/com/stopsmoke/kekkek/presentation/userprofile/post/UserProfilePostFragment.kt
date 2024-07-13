package com.stopsmoke.kekkek.presentation.userprofile.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.databinding.FragmentUserProfilePostBinding
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.userprofile.UserProfileViewModel
import com.stopsmoke.kekkek.presentation.userprofile.post.adapter.UserPostListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserProfilePostFragment : Fragment(), ErrorHandle {

    private var _binding: FragmentUserProfilePostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserProfileViewModel by activityViewModels()

    private lateinit var userPostListAdapter: UserPostListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfilePostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeRecyclerViewItem()
    }

    private fun setupRecyclerView() = with(binding.root) {
        userPostListAdapter = UserPostListAdapter {
            viewModel.navigatePostDetailScreen(it.id)
        }
        adapter = userPostListAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeRecyclerViewItem() = lifecycleScope.launch {
        viewModel.posts.collectLatest { postsResult ->
            when (postsResult) {
                is Result.Error -> errorExit(findNavController())
                Result.Loading -> {}
                is Result.Success ->  userPostListAdapter.submitData(postsResult.data)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}