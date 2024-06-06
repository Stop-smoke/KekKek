package com.stopsmoke.kekkek.presentation.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentMyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding: FragmentMyBinding get() = _binding!!

    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMyBookmark.setOnClickListener {
            findNavController().navigate("bookmark")
        }

        initView()
        initListener()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbarMy)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "My Toolbar Title"
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initListener() = with(binding) {
        tvMyWriting.setOnClickListener {
            findNavController().navigate("my_post")
        }
        tvMyComment.setOnClickListener {
            findNavController().navigate("my_comment")
        }
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }
    }


    private fun onBind(myUiState: MyUiState) = with(binding) {
        when (myUiState.myLoginUiState) {
            is MyLoginStatusState.NeedLoginUiState -> { // 로그인 필요

            }

            is MyLoginStatusState.LoggedUiState.MyIdLoggedUiState -> { //로그인 성공
                val myItem: MyItem = myUiState.myLoginUiState.myItem

                with(binding) {
                    ivMyProfile.load(myItem.profileImg) {
                        crossfade(true)
//                    placeholder(R.drawable.placeholder) 로딩중 띄우나?
//                    error(R.drawable.error) 오류시 띄우나?
                    }
                    tvMyName.text = myItem.name
                    tvMyRank.text = "랭킹 ${myItem.rank}위"

                    myItem.myWriting.let {
                        tvMyWritingNum.text = it.writing.toString()
                        tvMyCommentNum.text = it.comment.toString()
                        tvMyBookmarkNum.text = it.bookmark.toString()
                    }

                    tvMyAchievementNum.text = "${myItem.achievementNum} / 83"
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_my_toolbar, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle menu item clicks here
        when (item.itemId) {
            R.id.toolbar_my_bell -> {}
            R.id.toolbar_my_setting -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MyFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}