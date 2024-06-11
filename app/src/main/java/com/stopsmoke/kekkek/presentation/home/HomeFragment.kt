package com.stopsmoke.kekkek.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.databinding.FragmentHomeBinding
import com.stopsmoke.kekkek.presentation.test.TestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val sharedViewModel by activityViewModels<TestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initView()

    }

    private fun initView() = with(binding) {//클릭 시 이동 이벤트 처리 추가해야함
        initToolbar()

        sharedViewModel.testResult.observe(viewLifecycleOwner) { totalScore ->
            when(totalScore) {
                in 8..13 -> {
                    tvHomeTestDegree.text = "담배 비중독 상태🙂"
                }
                in 14..19 -> {
                    tvHomeTestDegree.text = "담배 의존 상태😥"
                }
                else -> {
                    tvHomeTestDegree.text = "담배 중독 상태😱"
                }
            }
            ivHomeTest.text = "다시 검사하기"
        }

        clHomeRank.setOnClickListener {
            findNavController().navigate("ranking_map")
        }

        binding.clHomeSavedMoney.setOnClickListener {
            navigateToAttainmentsFragment()
        }

        binding.ivHomeTest.setOnClickListener {
            findNavController().navigate("test_page")
        }
    }

    private fun initToolbar() {
        binding.toolbarHome.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_home_bell -> {
                    findNavController().navigate("notification")
                }
            }
            true
        }
    }


    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
                }
        }


        when (userData) {
            is Result.Error -> {

            }

            is Result.Loading -> {

            }

            is Result.Success -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    userData.data.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                        .collectLatest { user ->
                            viewModel.updateUserData(user)
                        }
                }
            }
        }
    }

    private fun onBind(uiState: HomeUiState) = with(binding) {
        uiState.homeItem.let {
            tvHomeSavedMoneyNum.text = "${it.savedMoney} 원"
            tvHomeSavedLifeNum.text = "${it.savedLife} 일"
            tvHomeRankNum.text = "${it.rank} 위"
            tvHomeTestDegree.text = it.addictionDegree

            tvHomeTimerNum.text = it.timerString
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_home_toolbar, menu)
//        return super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle menu item clicks here
//        when (item.itemId) {
//            R.id.toolbar_home_bell -> {}
//        }
//        return super.onOptionsItemSelected(item)
//    }

    fun navigateToAttainmentsFragment() {
        val navController = findNavController()
        navController.navigate("attainments")
    }
}
