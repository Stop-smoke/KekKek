package com.stopsmoke.kekkek.presentation.my.smokingsetting

import SmokingSettingDialogFragment
import SmokingSettingPriceDialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentSmokingSettingBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.invisible

class SmokingSettingFragment : Fragment(), ErrorHandle {

    private var _binding: FragmentSmokingSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SmokingSettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmokingSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        uiState.collectLatestWithLifecycle(lifecycle) { uiState ->
            when (uiState) {
                SmokingSettingUiState.InitUiState -> {}
                is SmokingSettingUiState.NormalUiState -> {
                    bind(uiState.item)
                }
                SmokingSettingUiState.ErrorExit -> errorExit(findNavController())
                SmokingSettingUiState.ErrorMissing -> errorExit(findNavController())
            }
        }

    }

    private fun bind(item: SmokingSettingItem) = with(binding){
        tvSmokingsettingValuePerday.text = item.dailyCigarettesSmoked.toString() + " 개비"
        tvSmokingsettingValuePerpack.text = item.packCigaretteCount.toString() + " 개비"
        tvSmokingsettingValuePerprice.text = item.packPrice.toString() + " 원"

        initButtons(item)
    }


    private fun initButtons(item: SmokingSettingItem) {
        binding.includeSmokingSettingAppBar.ivSmokingSettingBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val perDay = item.dailyCigarettesSmoked.toString()
        val perPack = item.packCigaretteCount.toString()
        val packPrice = item.packPrice.toString()

        binding.btnSmokingsettingPerday.setOnClickListener {
            showSmokingSettingDialog("설정하기 : 하루에 피는 담배 개비 수", perDay) { newValue ->
                viewModel.updateUserConfig(SmokingSettingType.DailyCigarettesSmoked, newValue.toInt())
            }
        }
        binding.btnSmokingsettingPerpack.setOnClickListener {
            showSmokingSettingDialog("설정하기 : 한 갑에 든 담배 개비 수", perPack) { newValue ->
                viewModel.updateUserConfig(SmokingSettingType.PackCigaretteCount, newValue.toInt())
            }
        }
        binding.btnSmokingsettingPerprice.setOnClickListener {
            showSmokingSettingPriceDialog("설정하기 : 한 갑당 가격", packPrice) { newValue ->
                viewModel.updateUserConfig(SmokingSettingType.PackPrice, newValue.toInt())
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