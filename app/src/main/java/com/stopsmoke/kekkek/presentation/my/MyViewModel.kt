package com.stopsmoke.kekkek.presentation.my

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    val userData = userRepository.getUserData()
        .catch {
            it.printStackTrace()
        }

    val activities = userRepository.getActivities().asResult()
}
