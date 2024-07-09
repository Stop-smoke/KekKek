package com.stopsmoke.kekkek.presentation.progress

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentCircularProgressDialog

class CircularProgressDialogFragment : DialogFragment() {
    private var _binding: FragmentCircularProgressDialog? = null
    private val binding: FragmentCircularProgressDialog get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.TransparentBackgroundDialogFragmentStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setCancelable(false)
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { dia, keyCode, event ->
                true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCircularProgressDialog.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FragmentCircularProgressDialog"
    }
}