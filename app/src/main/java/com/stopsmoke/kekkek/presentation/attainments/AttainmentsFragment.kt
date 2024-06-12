package com.stopsmoke.kekkek.presentation.attainments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentAttainmentsBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.visible

class AttainmentsFragment : Fragment() {

    private var _binding: FragmentAttainmentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AttainmentsViewModel


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttainmentsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AttainmentsViewModel::class.java]

        // 성과 데이터 업데이트
        viewModel.cigarettesNotSmoked.observe(viewLifecycleOwner) { cigarettes ->
                binding.tvAttainmentsDescription1.text = "$cigarettes 개비"
        }

        viewModel.moneySaved.observe(viewLifecycleOwner) { money ->
            binding.tvAttainmentsDescription2.text = "$money 원"
        }

        viewModel.lifeExtendedTime.observe(viewLifecycleOwner) { lifeExtenedTime ->
            binding.tvAttainmentsDescription3.text = lifeExtenedTime
        }

        viewModel.elapsedDays.observe(viewLifecycleOwner, Observer { days ->
            binding.tvAttainmentsDay.text = "${days}일"
        })

        viewModel.elapsedTime.observe(viewLifecycleOwner, Observer { time ->
            binding.tvAttainmentsTime.text = time
        })


        binding.includeAttainmentsAppBar.ivAttainmentsBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
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

