package com.stopsmoke.kekkek.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentHomeBinding
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), ErrorHandle {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private val timerStopDialog: HomeTimerStopDialogFragment by lazy {
        HomeTimerStopDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        checkNotificationPermission()
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

    override fun onResume() {
        super.onResume()
        activity?.visible()
    }

    private fun initView() = with(binding) {//클릭 시 이동 이벤트 처리 추가해야함
        viewModel.getAllUserData()
        initToolbar()

        binding.clHomeSavedMoney.setOnClickListener {
            findNavController().navigate("attainments")
        }

        binding.ivHomeTest.setOnClickListener {
            findNavController().navigate("test_page")
        }

        viewModel.user.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is User.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }

                is User.Guest -> {
                }

                is User.Registered -> {
                    viewModel.getMyRank()
                    if (it.cigaretteAddictionTestResult == null) {
                        tvHomeTestDegree.text = "테스트 필요"
                        ivHomeTest.text = "검사하기"
                    } else {
                        tvHomeTestDegree.text = it.cigaretteAddictionTestResult
                        ivHomeTest.text = "다시 검사하기"

                    }
                }

                else -> {}
            }
        }

        binding.clHomeTip.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_home_tip)
        }

        binding.clHomeCenter.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_home_center)
        }

        binding.clHomeRanking.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_rankingList)
        }

        binding.clHomeSavedMoney.setOnClickListener {
            navigateToAttainmentsFragment()
        }

        binding.ivHomeTest.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_nav_smoking_addiction_test_graph)
        }

        clHomeNotice.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_notice_list)
        }

        viewModel.updateUserData()
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
                    when (state) {
                        is HomeUiState.NormalUiState -> onBind(state)
                        is HomeUiState.ErrorExit -> {
                            errorExit(findNavController())
                        }
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noticeBanner.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { noticePost ->
                    binding.tvHomeNoticeTitle.text = noticePost.title
                }
        }

        viewModel.userRankingList.collectLatestWithLifecycle(lifecycle) {
            viewModel.getMyRank()
        }

        viewModel.myRank.collectLatestWithLifecycle(lifecycle) {
            binding.tvHomeRankNum.text = "${viewModel.myRank.value}위"
        }
    }

    private fun onBind(uiState: HomeUiState.NormalUiState) = with(binding) {
        uiState.homeItem.let {
            tvHomeSavedMoneyNum.text = it.savedMoney.toLong().toString() + " 원"
//            tvHomeSavedLifeNum.text = formatToOneDecimalPlace(it.savedLife) + " 일"
//            tvHomeRankNum.text = "${it.rank} 위"
            tvHomeTimerNum.text = it.timeString
        }

        if (uiState.startTimerSate) {
            ivHomeTimerController.setImageResource(R.drawable.ic_stop)
            viewModel.startTimer()
        } else if (!uiState.startTimerSate) {
            ivHomeTimerController.setImageResource(R.drawable.ic_home_start)
            viewModel.stopTimer()
        }

        initTimerControllerListener()
    }

    private fun initTimerControllerListener() {
        binding.ivHomeTimerController.setOnClickListener {
            if ((viewModel.uiState.value as HomeUiState.NormalUiState).startTimerSate
            ) {
                timerStopDialog.show(childFragmentManager, "timerStopDialog")
            } else {
                viewModel.setStartUserHistory()
            }
        }
    }

    private fun formatToOneDecimalPlace(value: Double): String {
        return String.format("%.1f", value)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToAttainmentsFragment() {
        val navController = findNavController()
        navController.navigate("attainments")
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            // 푸쉬 권한 없음
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                200
            )
        }
    }
}
