package com.stopsmoke.kekkek.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.oAuthCredential
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.model.User
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.authorization.google.GoogleAuthorization
import com.stopsmoke.kekkek.authorization.google.GoogleAuthorizationCallbackListener
import com.stopsmoke.kekkek.authorization.kakao.KakaoAuthorization
import com.stopsmoke.kekkek.authorization.kakao.KakaoAuthorizationCallbackListener
import com.stopsmoke.kekkek.databinding.FragmentAuthenticationBinding
import com.stopsmoke.kekkek.domain.model.ProfileImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment : Fragment(), KakaoAuthorizationCallbackListener,
    GoogleAuthorizationCallbackListener {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding get() = _binding!!

    private val viewModel: AuthenticationViewModel by viewModels()

    private lateinit var kakaoAuthorization: KakaoAuthorization
    private lateinit var googleAuthorization: GoogleAuthorization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleAuthorization = GoogleAuthorization(this).apply {
            registerCallbackListener(this@AuthenticationFragment)
        }
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
            kakaoAuthorization = KakaoAuthorization().apply {
                registerCallbackListener(this@AuthenticationFragment)
                loginKakao(requireContext())
            }
        }

        binding.flLoginGoogle.setOnClickListener {
            googleAuthorization.launchGoogleAuthActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        kakaoAuthorization.unregisterCallbackListener()
        googleAuthorization.unregisterCallbackListener()
        _binding = null
    }

    override fun onSuccess(token: OAuthToken, user: User) {
        val providerId = "oidc.kakao"
        val credential = oAuthCredential(providerId) {
            idToken = token.idToken
            accessToken = token.accessToken
        }
        val auth = Firebase.auth
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                com.stopsmoke.kekkek.domain.model.User(
                    uid = authResult.user?.uid ?: return@addOnSuccessListener,
                    name = user.kakaoAccount?.name ?: getString(R.string.login_default_nickname),
                    location = null,
                    profileImage = ProfileImage.Default
                ).let {
                    viewModel.updateUserData(it)
                }

                authResult.user?.displayName?.let {
                    Toast.makeText(requireContext(), "${it}님 환영합니다", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "로그인을 실패하였습니다", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }

    override fun onSuccess(user: FirebaseUser) {
        val domainUser = com.stopsmoke.kekkek.domain.model.User(
            uid = user.uid,
            name = user.displayName ?: getString(R.string.login_default_nickname),
            location = null,
            profileImage = ProfileImage.Default
        )
        viewModel.updateUserData(domainUser)
        Toast.makeText(requireContext(), "${user.displayName}님 환영합니다", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(t: Throwable?) {
        if (context != null) {
            Toast.makeText(context, "로그인 에러", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AuthenticationFragment()
    }
}