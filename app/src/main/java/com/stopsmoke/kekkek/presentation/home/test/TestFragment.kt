package com.stopsmoke.kekkek.presentation.home.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    val questionnaireResults = QuestionnaireResults()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

        activity?.onBackPressedDispatcher?.addCallback(
            object : OnBackPressedCallback(binding.viewpagerTest.currentItem == 0) {
                override fun handleOnBackPressed() {
                    val nextItem = binding.viewpagerTest.currentItem - 1
                    binding.viewpagerTest.setCurrentItem(nextItem, true)
                }

            }
        )
    }

    private fun setupView() {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle) {
            val nextItem = binding.viewpagerTest.currentItem + 1
            binding.viewpagerTest.setCurrentItem(nextItem, true)
        }
        binding.viewpagerTest.adapter = viewPagerAdapter
        binding.viewpagerTest.isUserInputEnabled = false // 사용자가 페이지를 넘기지 못하도록 만듬
    }

    fun moveToNextQuestionPage() {
        val nextItem = binding.viewpagerTest.currentItem + 1
        if (nextItem < (viewPagerAdapter.itemCount)) {
            binding.viewpagerTest.setCurrentItem(nextItem, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class QuestionnaireResults {
    var totalResult = 0

    fun addResponses(result: Int) {
        totalResult += result
    }
}