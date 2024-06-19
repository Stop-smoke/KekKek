package com.stopsmoke.kekkek.presentation.attainments

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
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentAttainmentsBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.MainActivity
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AttainmentsFragment : Fragment() {

    private var _binding: FragmentAttainmentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AttainmentsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttainmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateUserData()
        initAppbar()
        initViewModel()
    }


    private fun initAppbar() {
        requireActivity().findViewById<ImageView>(R.id.iv_attainments_back).setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { item ->
                    onBind(item)
                }
        }
    }

    private fun onBind(item: AttainmentsItem) = with(binding) {
        tvAttainmentsTimerNum.text = item.timeString
        tvAttainmentsDescriptionLife.text = formatToOneDecimalPlace(item.savedLife) + "일"
        tvAttainmentsDescriptionMoney.text =
            item.savedMoney.toLong().toString().chunked(3).joinToString(",") + "원"
        tvAttainmentsDescriptionCigarette.text = formatToOneDecimalPlace(item.savedCigarette) + "개비"
    }

    private fun formatToOneDecimalPlace(value: Double): String {
        return String.format("%.1f", value)
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.visible()
        _binding = null
    }


}

