package com.stopsmoke.kekkek.presentation.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    // 데이터를 이쪽에 일단 몰빵...
    private val _testResult: MutableLiveData<Int> = MutableLiveData()
    val testResult: LiveData<Int> get() = _testResult

//    private val _firstTest = SingleLiveEvent<Any>()

    private var score = 0

    fun addScore(currentScore: Int) {
        score += currentScore
    }

    fun getTotalScore() {
        _testResult.postValue(score)
    }

    fun clearScore() {
        score = 0
    }

    // 질문 제목
    val questionTitles = listOf(
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
    val questionAnswers = listOf(
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

    val results = listOf(
        listOf(
            "가장 쉽게 금연할 수 있는 상태",
            "흡연량과 흡연 시간이 늘어날수록 니코틴에 대한 의존도는 높아집니다. 오늘부터 금연 성공을 이어가세요!",
            R.drawable.ic_test_good
        ),
        listOf(
            "금연을 시작해야 할 상태",
            "구체적인 증상이 나타나지 않아 큰 어려움 없이 금연할 수 있지만, 재흡연도 쉬운 시기입니다.",
            R.drawable.ic_test_soso
        ),
        listOf(
            "니코틴에 대한 의존이 이미 심한 상태",
            "심한 금단증상으로 금연을 이어가기 힘든 경우 전문가의 도움을 받아보시는 것을 추천 드립니다.",
            R.drawable.ic_test_bad
        )
    )

    fun updateCigaretteAddictionTestResult(result: String) {
        viewModelScope.launch {
            val userRegistered =
                userRepository.getUserData().firstOrNull() as? User.Registered ?: return@launch
            userRepository.setUserData(userRegistered.copy(cigaretteAddictionTestResult = result))
        }
    }

}