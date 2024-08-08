package com.stopsmoke.kekkek.presentation.authentication.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.DialogTermBinding
import com.stopsmoke.kekkek.presentation.onboarding.navigateToOnboardingGraph

class TermBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: DialogTermBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogTermBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListner()
        allTermCheck()
    }

    private fun allTermCheck() {
        with(binding) {
            cbTermAllAccept.setOnCheckedChangeListener { _, isChecked ->
                setAllCheckbox(isChecked)
            }

            listOf(cbTermAge, cbTermPrivate, cbTermService, cbTermCommunity).forEach { checkbox ->
                checkbox.setOnCheckedChangeListener { _, _ -> updateAllAcceptCheckbox() }
            }
        }
    }

    private fun setAllCheckbox(isChecked: Boolean) {
        with(binding) {
            listOf(
                cbTermAge,
                cbTermPrivate,
                cbTermService,
                cbTermCommunity
            ).forEach { it.isChecked = isChecked }
        }
    }

    private fun updateAllAcceptCheckbox() {
        with(binding) {
            val cbList = listOf(cbTermAge, cbTermPrivate, cbTermService, cbTermCommunity)
            cbTermAllAccept.setOnCheckedChangeListener(null)
            cbTermAllAccept.isChecked = cbList.all { it.isChecked }
            btnTermAccept.isEnabled = cbList.all { it.isChecked }
            cbTermAllAccept.setOnCheckedChangeListener { _, isChecked ->
                setAllCheckbox(isChecked)
            }
        }
    }

    private fun setOnClickListner() {
        with(binding) {
            val cbList = listOf(cbTermAge, cbTermPrivate, cbTermService, cbTermCommunity)

            val clToCbList = listOf(
                clTermAllAccept to cbTermAllAccept,
                clTermAge to cbTermAge,
                clTermPrivate to cbTermPrivate,
                clTermService to cbTermService,
                clTermCommunity to cbTermCommunity
            )

            cbTermAllAccept.setOnCheckedChangeListener(null)
            cbTermAllAccept.isChecked = cbList.all { it.isChecked }
            cbTermAllAccept.setOnCheckedChangeListener { _, isChecked ->
                setAllCheckbox(isChecked)
            }

            clToCbList.forEach {(constraintLayout, checkBox) ->
                constraintLayout.setOnClickListener {
                    checkBox.isChecked = !checkBox.isChecked
                }
            }

            tvTermPrivateDesc.setOnClickListener {
                showTermDetail(TermType.PRIVATE)
            }

            tvTermServiceDesc.setOnClickListener {
                showTermDetail(TermType.SERVICE)
            }

            tvTermCommunityDesc.setOnClickListener {
                showTermDetail(TermType.COMMUNITY)
            }

            btnTermAccept.setOnClickListener {
                if (cbList.all { it.isChecked }) {
                    findNavController().navigateToOnboardingGraph()
                    dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.term_title),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showTermDetail(termType: TermType) {
        val termUrlMapList = listOf(
            TermType.PRIVATE to requireContext().getString(R.string.term_private_url),
            TermType.SERVICE to requireContext().getString(R.string.term_service_url),
            TermType.COMMUNITY to requireContext().getString(R.string.term_community_url)
        )

        val url: String = when(termType) {
            TermType.PRIVATE -> termUrlMapList.find { it.first == TermType.PRIVATE }?.second
            TermType.SERVICE -> termUrlMapList.find { it.first == TermType.SERVICE }?.second
            TermType.COMMUNITY -> termUrlMapList.find { it.first == TermType.COMMUNITY }?.second
        } ?: throw IllegalStateException("URL not found for TermType: $termType")

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}