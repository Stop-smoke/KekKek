package com.stopsmoke.kekkek.presentation.myWritingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentMyWritingListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyWritingListFragment : Fragment() {
    private var _binding: FragmentMyWritingListBinding? = null
    val binding: FragmentMyWritingListBinding get() = _binding!!

    private val listAdapter: MyWritingListAdapter by lazy {
        MyWritingListAdapter()
    }

    private val viewModel: MyWritingLIstViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyWritingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        initAppBar()
        rvMyWritingList.adapter = listAdapter
        rvMyWritingList.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.updateUserState()
    }

    private fun initAppBar() {
        val ivMyPostBack =
            requireActivity().findViewById<ImageView>(R.id.iv_my_post_back)

        ivMyPostBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            myWritingPosts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { myWritingPosts ->
                    listAdapter.submitData(myWritingPosts)
                }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}