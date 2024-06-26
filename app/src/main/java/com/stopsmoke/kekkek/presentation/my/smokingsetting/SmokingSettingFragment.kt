package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSmokingSettingBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle

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
//        observeUiState()
    }

    private fun initButtons() {
        binding.includeSmokingSettingAppBar.ivSmokingSettingBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSmokingsettingPerday.setOnClickListener {
            showEditDialog("설정하기 : 하루에 피는 담배 개비 수", viewModel.perDay.value) { newValue ->
                viewModel.updateSmokingPerDay(newValue)

            }
        }
        binding.btnSmokingsettingPerpack.setOnClickListener {
            showEditDialog("설정하기 : 한 팩 당 담배 개비 수", viewModel.perPack.value) { newValue ->
                viewModel.updateSmokingPerPack(newValue)
            }
        }
        binding.btnSmokingsettingPerprice.setOnClickListener {
            showEditDialog("설정하기 : 한 팩 당 가격", viewModel.packPrice.value) { newValue ->
                viewModel.updateSmokingPackPrice(newValue)
            }
        }
    }

    private fun showEditDialog(title: String, currentValue: String, onSave: (String) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText_dialog)
        editText.setText(currentValue)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                onSave(editText.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

//    private fun observeUiState() {
//        viewModel.perDayUiState.collectLatestWithLifecycle(viewLifecycleOwner.lifecycle) {
//            handleUiState()
//
//        }
//    }


    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}