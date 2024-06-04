package com.stopsmoke.kekkek.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

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

        binding.toolbarHome.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.toolbar_home_bell -> {
                    findNavController().navigate("notification")
                }
            }
            true
        }

        initViewModel()
        binding.clHomeSavedMoney.setOnClickListener {
            navigateToAttainmentsFragment()
        }

        binding.ivHomeTest.setOnClickListener {
            findNavController().navigate("test_page")
        }
    }

    private fun initView() = with(binding) {//클릭 시 이동 이벤트 처리 추가해야함
    }


    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    onBind(state)
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
