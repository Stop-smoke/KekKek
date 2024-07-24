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
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.databinding.FragmentHomeBinding
import com.stopsmoke.kekkek.presentation.attainments.navigateToAttainmentsScreen
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.home.center.navigateToHomeCenterScreen
import com.stopsmoke.kekkek.presentation.home.dialog.HomeTimerStartDialogFragment
import com.stopsmoke.kekkek.presentation.home.dialog.HomeTimerStopDialogFragment
import com.stopsmoke.kekkek.presentation.home.tip.navigateToHomeTipScreen
import com.stopsmoke.kekkek.presentation.notification.navigateToNotificationScreen
import com.stopsmoke.kekkek.presentation.post.notice.navigateToPostNoticeScreen
import com.stopsmoke.kekkek.presentation.ranking.navigateToRankingScreen
import com.stopsmoke.kekkek.presentation.smokingaddictiontest.navigateToSmokingAddictionTestGraph
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), ErrorHandle {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private val timerStartDialog: HomeTimerStartDialogFragment by lazy {
        HomeTimerStartDialogFragment()
    }

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
    ): View {
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
            findNavController().navigateToHomeTipScreen()
        }

        binding.clHomeCenter.setOnClickListener {
            findNavController().navigateToHomeCenterScreen()
        }

        binding.clHomeRanking.setOnClickListener {
            findNavController().navigateToRankingScreen()
        }

        binding.clHomeSavedMoney.setOnClickListener {
            findNavController().navigateToAttainmentsScreen()
        }

        binding.ivHomeTest.setOnClickListener {
            findNavController().navigateToSmokingAddictionTestGraph()
        }

        clHomeNotice.setOnClickListener {
            findNavController().navigateToPostNoticeScreen()
        }

        viewModel.updateUserData()

    }


    private fun initToolbar() {
        binding.toolbarHome.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_home_bell -> {
                    findNavController().navigateToNotificationScreen()
                }
            }
            true
        }
    }


    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            homeUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
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
                .collectLatest { noticePostResult ->
                    when (noticePostResult) {
                        is Result.Error -> {
                            noticePostResult.exception?.printStackTrace()
                            errorExit(findNavController())
                        }

                        Result.Loading -> {}
                        is Result.Success -> binding.tvHomeNoticeTitle.text =
                            noticePostResult.data.title
                    }

                }
        }

        viewModel.userList.collectLatestWithLifecycle(lifecycle) {
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
//            tvHomeTimerNum.text = it.timeString
            tvHomeTimerNum.text = "설정해주세요"
        }

        if (uiState.startTimerSate) {
//            ivHomeTimerController.setImageResource(R.drawable.ic_stop)
            viewModel.startTimer()
            binding.tvHomeTimer.text = "금연한 지"
            tvHomeTimerNum.text = uiState.homeItem.timeString

        } else if (!uiState.startTimerSate) {
//            ivHomeTimerController.setImageResource(R.drawable.ic_home_start)
            binding.tvHomeTimer.text = "금연 정보를"
            viewModel.stopTimer()
        }

        initTimerControllerListener()

        val motivationStringList = resources.getStringArray(R.array.home_motivation)
        tvHomeMotivation.text = motivationStringList.random()
    }

    private fun initTimerControllerListener() {
        binding.clHomeToptext.setOnClickListener {
            if ((viewModel.homeUiState.value as HomeUiState.NormalUiState).startTimerSate
            ) {
                timerStopDialog.show(childFragmentManager, "timerStopDialog")
            } else {
                timerStartDialog.show(childFragmentManager, "timerStartDialog")
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
