package com.stopsmoke.kekkek.presentation.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentHomeBinding
import com.stopsmoke.kekkek.domain.model.CommentFilter
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.shared.SharedViewModel
import com.stopsmoke.kekkek.presentation.test.TestViewModel
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChanged
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
    }

    override fun onResume() {
        super.onResume()
        activity?.visible()
    }

    private fun initView() = with(binding) {//클릭 시 이동 이벤트 처리 추가해야함
        initToolbar()

        binding.clHomeSavedMoney.setOnClickListener {
            findNavController().navigate("attainments")
        }

        binding.ivHomeTest.setOnClickListener {
            findNavController().navigate("test_page")
        }

        viewModel.user.distinctUntilChanged().collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is User.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }

                is User.Guest -> {
                    Toast.makeText(requireContext(), "Guest", Toast.LENGTH_SHORT).show()
                }

                is User.Registered -> {
                    if (it.cigaretteAddictionTestResult == null) {
                        tvHomeTestDegree.text = "테스트 필요"
                        ivHomeTest.text = "검사하기"
                    } else {
                        tvHomeTestDegree.text = it.cigaretteAddictionTestResult
                        ivHomeTest.text = "다시 검사하기"
                    }
                }
            }
        }

        binding.clHomeTip.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_home_tip)
        }

        binding.clHomeCenter.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_home_center)
        }

        binding.clHomeSavedMoney.setOnClickListener {
            navigateToAttainmentsFragment()
        }

        binding.ivHomeTest.setOnClickListener {
            findNavController().navigate("test_page")
        }

        clHomeNotice.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_notice_list)
        }

        viewModel.updateUserData()
        initTimerControllerListener()
    }


    private fun initTimerControllerListener() {
        val startDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_play)
        val stopDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_stop)

        val currentDrawable = binding.ivHomeTimerController.drawable

        binding.ivHomeTimerController.setOnClickListener {
            if (viewModel.uiState.value.startTimerSate
            ) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("타이머 초기화")
                builder.setMessage("타이머를 초기화하시겠습니까?")
                builder.setIcon(R.drawable.ic_timer)

                val listener = DialogInterface.OnClickListener { _, type ->
                    when (type) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            viewModel.setStopUserHistory()
                        }
                    }
                }

                builder.setPositiveButton("예", listener)
                builder.setNegativeButton("아니요", null)
                builder.show()
            } else {
                viewModel.setStartUserHistory()
            }
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noticeBanner.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { noticePost ->
                    binding.tvHomeNoticeTitle.text = noticePost.title
                }
        }
    }

    private fun onBind(uiState: HomeUiState) = with(binding) {
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
}
