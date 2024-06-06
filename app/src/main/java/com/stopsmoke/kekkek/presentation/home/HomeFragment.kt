package com.stopsmoke.kekkek.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

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
        binding.clHomeSavedMoney.setOnClickListener {
            navigateToAttainmentsFragment()
        }

        binding.ivHomeTest.setOnClickListener {
            findNavController().navigate("test_page")
        }
    }

    private fun initView() = with(binding) {//클릭 시 이동 이벤트 처리 추가해야함
        initToolbar()

    }

    private fun initToolbar(){
        binding.toolbarHome.setOnMenuItemClickListener {
            when(it.itemId) {
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

        viewLifecycleOwner.lifecycleScope.launch {
            userData?.collectLatest {
                Log.d("userData", it.toString())
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
