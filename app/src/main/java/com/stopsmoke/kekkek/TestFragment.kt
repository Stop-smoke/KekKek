package com.stopsmoke.kekkek

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.stopsmoke.kekkek.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    val questionnaireResults = QuestionnaireResults()

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTestBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.viewpagerTest.adapter = ViewPagerAdapter(requireActivity()) // 이게 맞나..?
        viewPager.isUserInputEnabled = false // 화면이 넘어가지 못하도록 막음
    }

    fun moveToNextQuestionPage() {
        if(viewPager.currentItem == 7) {
            val fragment = TestResultFragment()
            fragment.result = questionnaireResults.totalResult
            parentFragmentManager.commit {
                replace(R.id.main,fragment)
                    .commit()
            }
        } else {
            val nextItem = viewPager.currentItem + 1
            if(nextItem < (viewPager.adapter?.itemCount ?: 0)) {
                viewPager.setCurrentItem(nextItem,true)
            }
        }
    }
}

class QuestionnaireResults {
    var totalResult = 0

    fun addResponses(result: Int) {
        totalResult += result
    }
}