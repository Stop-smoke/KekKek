package com.stopsmoke.kekkek.presentation.my.smokingsetting

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.stopsmoke.kekkek.R

class SmokingSettingDialogFragment : DialogFragment() {

    interface OnSaveListener {
        fun onSave(newValue: String)
    }

    private var title: String? = null
    private var currentValue: String? = null
    private var onSaveListener: OnSaveListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_common_et_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.et_dialog_content)
        val dialogTitle = view.findViewById<TextView>(R.id.tv_dialog_title)
        val cancelButton = view.findViewById<AppCompatButton>(R.id.btn_dialog_cancel)
        val finishButton = view.findViewById<AppCompatButton>(R.id.btn_dialog_finish)

        dialogTitle.text = title
        editText.setText(currentValue)

        cancelButton.setOnClickListener {
            dismiss()
        }

        finishButton.setOnClickListener {
            val newValue = editText.text.toString()
            onSaveListener?.onSave(newValue)
            dismiss()
        }
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setCurrentValue(currentValue: String) {
        this.currentValue = currentValue
    }

    fun setOnSaveListener(onSaveListener: OnSaveListener) {
        this.onSaveListener = onSaveListener
    }

    companion object {
        const val TAG = "SmokingSettingDialogFragment"

        fun newInstance(title: String, currentValue: String, onSaveListener: OnSaveListener): SmokingSettingDialogFragment {
            val fragment = SmokingSettingDialogFragment()
            fragment.setTitle(title)
            fragment.setCurrentValue(currentValue)
            fragment.setOnSaveListener(onSaveListener)
            return fragment
        }
    }
}
