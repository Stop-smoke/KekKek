package com.stopsmoke.kekkek.presentation.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentOnboardingPerpackBinding
import com.stopsmoke.kekkek.invisible


class OnboardingPerpackFragment : Fragment() {

    private var _binding: FragmentOnboardingPerpackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOnboardingPerpackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOnboardingNext.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding_perpack_to_onboarding_price)
        }

        binding.sliderOnboardingPerpack.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // SeekBar의 값이 변경될 때 호출됩니다.
                binding.tvSliderValue.text = progress.toString() + "개비"
                binding.btnOnboardingNext.isEnabled = progress > 0
                Log.d("ssssss", progress.toString())
                viewModel.updateCigarettesPerPack(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 사용자가 SeekBar 터치를 시작할 때 호출됩니다.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 사용자가 SeekBar 터치를 멈출 때 호출됩니다.
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}