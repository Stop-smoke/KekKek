package com.stopsmoke.kekkek.presentation.home.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentQuestionBinding
import com.stopsmoke.kekkek.invisible

class QuestionFragment : Fragment() {

    private var questionType = 0 // 초기값 → 0 ~ 7

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private var result = 0

    private lateinit var testFragment: TestFragment

    // 질문 제목
    private val questionTitles = listOf(
        R.string.question1_title,
        R.string.question2_title,
        R.string.question3_title,
        R.string.question4_title,
        R.string.question5_title,
        R.string.question6_title,
        R.string.question7_title,
        R.string.question8_title
    )

    // 질문 응답
    private val questionAnswers = listOf(
        listOf(
            R.string.question1_answer1,
            R.string.question1_answer2,
            R.string.question1_answer3
        ),
        listOf(
            R.string.question2_answer1,
            R.string.question2_answer2,
            R.string.question2_answer3
        ),
        listOf(
            R.string.question3_answer1,
            R.string.question3_answer2,
            R.string.question3_answer3
        ),
        listOf(
            R.string.question4_answer1,
            R.string.question4_answer2,
            R.string.question4_answer3
        ),
        listOf(
            R.string.question5_answer1,
            R.string.question5_answer2,
            R.string.question5_answer3
        ),
        listOf(
            R.string.question6_answer1,
            R.string.question6_answer2,
            R.string.question6_answer3
        ),
        listOf(
            R.string.question7_answer1,
            R.string.question7_answer2,
            R.string.question7_answer3
        ),
        listOf(
            R.string.question8_answer1,
            R.string.question8_answer2,
            R.string.question8_answer3
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionType = it.getInt(QUESTION_TYPE) // 지금 몇번째 화면인가?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testFragment = (parentFragment as TestFragment)
        setupView()
        setupListener()
    }

    private fun setupView() {
        binding.run {
            tvQuestionMainText.text = getString(questionTitles[questionType])
            btnQuestionAnswer1.text = getString(questionAnswers[questionType][0])
            btnQuestionAnswer2.text = getString(questionAnswers[questionType][1])
            btnQuestionAnswer3.text = getString(questionAnswers[questionType][2])
        }
    }

    private fun setupListener() {
        with(binding) {
            btnQuestionAnswer1.setOnClickListener {
                // 점수: 0 + 다음 화면 넘어가기
                result = 0
                testFragment.questionnaireResults.addResponses(result)
                testFragment.moveToNextQuestionPage()
            }
            btnQuestionAnswer2.setOnClickListener {
                // 점수: 1 + 다음 화면 넘어가기
                result = 1
                testFragment.questionnaireResults.addResponses(result)
                testFragment.moveToNextQuestionPage()
            }
            btnQuestionAnswer3.setOnClickListener {
                // 점수: 2 + 다음 화면 넘어가기
                result = 2
                testFragment.questionnaireResults.addResponses(result)
                testFragment.moveToNextQuestionPage()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    companion object {
        private const val QUESTION_TYPE = "pageNum"

        fun newInstance(pageNum: Int): QuestionFragment {
            val fragment = QuestionFragment()
            val argument = Bundle()
            argument.putInt(QUESTION_TYPE, pageNum) // 새로운 페이지의 페이지 번호를 전달함
            fragment.arguments = argument
            return fragment
        }
    }
}