package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingSettingBinding
import com.stopsmoke.kekkek.invisible
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

//        일단 나중에
//        viewModel.perDay.collectLatestWithLifecycle(lifecycle){ perDay ->
//            binding.tvSmokingsettingValuePerday.text = perDay + "개비"
//        }
//        viewModel.perPack.collectLatestWithLifecycle(lifecycle){ perPack ->
//            binding.tvSmokingsettingValuePerpack.text = perPack + "개비"
//        }
//        viewModel.packPrice.collectLatestWithLifecycle(lifecycle){ packPrice ->
//            binding.tvSmokingsettingValuePerprice.text = packPrice + "원"
//        }
    }

    private fun initButtons() {
        binding.includeSmokingSettingAppBar.ivSmokingSettingBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSmokingsettingPerday.setOnClickListener {
            showEditDialog("설정하기 : 하루에 피는 담배 개비 수", viewModel.perDay.value) { newValue ->
                viewModel.updateSmokingPerDay(newValue)
                viewModel.updateUserConfig()
                Log.d("담배설정", newValue)
            }
        }
        binding.btnSmokingsettingPerpack.setOnClickListener {
            showEditDialog("설정하기 : 한 팩 당 담배 개비 수", viewModel.perPack.value) { newValue ->
                viewModel.updateSmokingPerPack(newValue)
                viewModel.updateUserConfig()
                Log.d("담배설정", newValue)
            }
        }
        binding.btnSmokingsettingPerprice.setOnClickListener {
            showEditDialog("설정하기 : 한 팩 당 가격", viewModel.packPrice.value) { newValue ->
                viewModel.updateSmokingPackPrice(newValue)
                viewModel.updateUserConfig()
                Log.d("담배설정", newValue)
            }
        }
    }

    private fun showEditDialog(title: String, currentValue: String, onSave: (String) -> Unit) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.fragment_common_et_dialog, null)
        val editText = dialogView.findViewById<EditText>(R.id.et_dialog_content)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialog_title)
        val cancelButton = dialogView.findViewById<AppCompatButton>(R.id.btn_dialog_cancel)
        val finishButton = dialogView.findViewById<AppCompatButton>(R.id.btn_dialog_finish)

        dialogTitle.text = title
        editText.setText(currentValue)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        finishButton.setOnClickListener {
            val newValue = editText.text.toString()
            onSave(newValue)
            dialog.dismiss()
        }
        dialog.show()
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