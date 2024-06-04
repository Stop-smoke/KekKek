package com.stopsmoke.kekkek.presentation.attainments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.stopsmoke.kekkek.R

class AttainmentsFragment : Fragment() {
    companion object {
        fun newInstance() = AttainmentsFragment()
    }

    private val viewModel: AttainmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_attainments, container, false)
        viewModel = ViewModelProvider(this).get(AttainmentsViewModel::class.java)

        // 성과 데이터 업데이트

    }
}