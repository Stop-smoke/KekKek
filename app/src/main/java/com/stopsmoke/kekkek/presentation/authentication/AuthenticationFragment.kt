package com.stopsmoke.kekkek.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.authorization.FirebaseAuthorizationEvent
import com.stopsmoke.kekkek.core.authorization.google.GoogleAuthorization
import com.stopsmoke.kekkek.core.authorization.kakao.KakaoAuthorization
import com.stopsmoke.kekkek.databinding.FragmentAuthenticationBinding
import com.stopsmoke.kekkek.presentation.authentication.dialog.TermBottomSheetDialog
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.home.navigateToHomeScreenWithClearBackStack
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.onboarding.OnboardingViewModel
import com.stopsmoke.kekkek.presentation.onboarding.model.AuthenticationUiState
import com.stopsmoke.kekkek.presentation.onboarding.navigateToOnboardingGraph
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class AuthenticationFragment : Fragment(), FirebaseAuthorizationEvent {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding get() = _binding!!

    private val viewModel: OnboardingViewModel by activityViewModels()

    private var kakaoAuthorization: KakaoAuthorization? = null
    private var googleAuthorization: GoogleAuthorization? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.invisible()
        initKakaoAuthorization()
        initGoogleAuthorization()
    }

    private fun initKakaoAuthorization() {
        kakaoAuthorization = KakaoAuthorization()
        kakaoAuthorization!!.registerCallbackListener(this@AuthenticationFragment)
    }

    private fun initGoogleAuthorization() {
        googleAuthorization = GoogleAuthorization(this)
        googleAuthorization!!.registerCallbackListener(this@AuthenticationFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.flLoginKakao.setOnClickListener {
            kakaoAuthorization?.loginKakao(requireContext())
        }

        binding.flLoginGoogle.setOnClickListener {
            googleAuthorization?.launchGoogleAuthActivity()
        }
        handleAuthenticationResult()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoAuthorization?.unregisterCallbackListener()
        googleAuthorization?.unregisterCallbackListener()
        googleAuthorization = null
        kakaoAuthorization = null
    }

    override fun onSuccess(user: FirebaseUser) {
        viewModel.updateUid(user.uid)
        viewModel.updateUserName(user.displayName ?: getString(R.string.login_default_nickname))
        Toast.makeText(requireContext(), "${user.displayName}님 환영합니다", Toast.LENGTH_SHORT).show()
        viewModel.registeredApp()
    }

    override fun onFailure(t: Throwable?) {
        if (context != null) {
            Toast.makeText(context, "로그인 에러", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleAuthenticationResult() {
        lifecycleScope.launch {
            viewModel.authenticationUiState
                .distinctUntilChanged()
                .collectLatestWithLifecycle(lifecycle, Lifecycle.State.CREATED) { uiState ->
                    when (uiState) {
                        is AuthenticationUiState.AlreadyUser -> {
                            findNavController().navigateToHomeScreenWithClearBackStack()
                        }

                        is AuthenticationUiState.NewMember -> {
                            val termDialog = TermBottomSheetDialog()
                            termDialog.show(childFragmentManager, termDialog.tag)
                        }

                        is AuthenticationUiState.Error -> {
                            Toast.makeText(requireContext(), "에러가 발생하였습니다", Toast.LENGTH_SHORT).show()
                        }

                        is AuthenticationUiState.Guest -> {  }

                        is AuthenticationUiState.Init -> { }
                    }
                }
        }
    }
}