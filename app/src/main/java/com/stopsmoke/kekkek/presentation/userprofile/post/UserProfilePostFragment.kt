package com.stopsmoke.kekkek.presentation.userprofile.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.databinding.FragmentUserProfilePostBinding
import com.stopsmoke.kekkek.presentation.userprofile.UserProfileViewModel
import com.stopsmoke.kekkek.presentation.userprofile.post.adapter.UserPostListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserProfilePostFragment : Fragment() {

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
        userPostListAdapter = UserPostListAdapter()
        adapter = userPostListAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeRecyclerViewItem() = lifecycleScope.launch {
        viewModel.posts.collectLatest {
            userPostListAdapter.submitData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}