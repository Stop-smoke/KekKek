package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentSmokingSettingBinding
import com.stopsmoke.kekkek.presentation.invisible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SmokingSettingFragment : Fragment() {

    private var _binding: FragmentSmokingSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SmokingSettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmokingSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        observeValue()
    }

    private fun observeValue() {
        lifecycleScope.launch {
            viewModel.perDay.collectLatest { perDay ->
                binding.tvSmokingsettingValuePerday.text = perDay + " 개비"
            }
        }
        lifecycleScope.launch {
            viewModel.perPack.collectLatest { perPack ->
                binding.tvSmokingsettingValuePerpack.text = perPack + " 개비"
            }
        }
        lifecycleScope.launch {
            viewModel.packPrice.collectLatest { perPrice ->
                binding.tvSmokingsettingValuePerprice.text = perPrice + " 원"
            }
        }
    }

    private fun initButtons() {
        binding.includeSmokingSettingAppBar.ivSmokingSettingBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSmokingsettingPerday.setOnClickListener {
            showSmokingSettingDialog("설정하기 : 하루에 피는 담배 개비 수", viewModel.perDay.value) { newValue ->
                viewModel.updateSmokingPerDay(newValue)
                viewModel.updateUserConfig()
                Log.d("담배설정", newValue)
            }
        }
        binding.btnSmokingsettingPerpack.setOnClickListener {
            showSmokingSettingDialog("설정하기 : 한 갑에 든 담배 개비 수", viewModel.perPack.value) { newValue ->
                viewModel.updateSmokingPerPack(newValue)
                viewModel.updateUserConfig()
                Log.d("담배설정", newValue)
            }
        }
        binding.btnSmokingsettingPerprice.setOnClickListener {
            showSmokingSettingPriceDialog("설정하기 : 한 갑당 가격", viewModel.packPrice.value) { newValue ->
                viewModel.updateSmokingPackPrice(newValue)
                viewModel.updateUserConfig()
                Log.d("담배설정", newValue)
            }
        }
    }

    private fun showSmokingSettingDialog(
        title: String,
        currentValue: String,
        onSave: (String) -> Unit
    ) {
        val dialogFragment = SmokingSettingDialogFragment.newInstance(
            title,
            currentValue,
            object : SmokingSettingDialogFragment.OnSaveListener {
                override fun onSave(newValue: String) {
                    onSave(newValue)
                }
            })
        dialogFragment.show(childFragmentManager, SmokingSettingDialogFragment.TAG)
    }

    private fun showSmokingSettingPriceDialog(
        title: String,
        currentValue: String,
        onSave: (String) -> Unit
    ) {
        val dialogFragment = SmokingSettingPriceDialogFragment.newInstance(
            title,
            currentValue,
            object : SmokingSettingPriceDialogFragment.OnSaveListener {
                override fun onSave(newValue: String) {
                    onSave(newValue)
                }
            })
        dialogFragment.show(childFragmentManager, SmokingSettingPriceDialogFragment.TAG)
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}