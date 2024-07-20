package com.stopsmoke.kekkek.presentation.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.stopsmoke.kekkek.databinding.FragmentCommonDialogBinding

class CommonDialogFragment : DialogFragment() {

    private var _binding: FragmentCommonDialogBinding? = null
    private val binding: FragmentCommonDialogBinding get() = _binding!!

    private lateinit var title: String
    private lateinit var description: String
    private lateinit var positiveText: String
    private lateinit var negativeText: String

    private var callback : CommonDialogListener? = null

    fun registerCallbackListener(listener: CommonDialogListener) {
        callback = listener
    }

    fun unregisterCallbackListener() {
        callback = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentArguments()
    }

    private fun initFragmentArguments() {
        title = arguments?.getString(TITLE_ARGUMENT) ?: ""
        description = arguments?.getString(DESCRIPTION_ARGUMENT) ?: ""
        positiveText = arguments?.getString(POSITIVE_TEXT_ARGUMENT) ?: ""
        negativeText = arguments?.getString(NEGATIVE_TEXT_ARGUMENT) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommonDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDialogTitle.text = title
        binding.tvDialogContent.text = description
        binding.btnDialogFinish.text = positiveText
        binding.btnDialogCancel.text = negativeText

        binding.btnDialogCancel.setOnClickListener {
            callback?.onNegative()
        }
        binding.btnDialogFinish.setOnClickListener {
            callback?.onPositive()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val TITLE_ARGUMENT = "title"
        private const val DESCRIPTION_ARGUMENT = "description"
        private const val POSITIVE_TEXT_ARGUMENT = "positive_text"
        private const val NEGATIVE_TEXT_ARGUMENT = "negative_text"

        fun newInstance(
            title: String,
            description: String,
            positiveText: String = "예",
            negativeText: String = "아니오"
        ): CommonDialogFragment {
            val args = Bundle().apply {
                putString(TITLE_ARGUMENT, title)
                putString(DESCRIPTION_ARGUMENT, description)
                putString(POSITIVE_TEXT_ARGUMENT, positiveText)
                putString(NEGATIVE_TEXT_ARGUMENT, negativeText)
            }
            val fragment = CommonDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}