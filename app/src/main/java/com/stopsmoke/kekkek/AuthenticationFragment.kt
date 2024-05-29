package com.stopsmoke.kekkek

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.stopsmoke.kekkek.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding get() = _binding!!

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val googleSighInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            requireContext(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("963210879703-ocaf574hbvf2gq9hahpmhhvmbgbvn50s.apps.googleusercontent.com")
                .requestId()
                .requestEmail()
                .requestProfile()
                .build()
        )
    }

    private lateinit var googleLoginResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initRegister()
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var keyHash = Utility.getKeyHash(requireContext())
        Log.e("key", "해시 키 값 : ${keyHash}")

        binding.ivAuthKakaoLogin.setOnClickListener {
            clickKakaoLoginButton()
        }

        binding.ivAuthGoogleLogin.setOnClickListener {
            clickGoogleLoginButton()
        }
    }

    private fun clickGoogleLoginButton() {
        val signInIntent = googleSighInClient.signInIntent
        googleLoginResult.launch(signInIntent)
    }


    private fun clickKakaoLoginButton() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("kakao", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("kakao", "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e("kakao1", "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        Log.e("kakao2", "${error.reason}")
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(
                        requireContext(),
                        callback = callback
                    )
                } else if (token != null) {
                    Log.i("kakao", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e("kakaouser", "사용자 정보 요청 실패", error)
                        } else if (user != null) { // kakao developer에서 동의항목 변경 시 조회
                            Log.i(
                                "kakaouser", "사용자 정보 요청 성공" +
                                        "\n회원번호: ${user.id}" +
                                        "\n이메일: ${user.kakaoAccount?.email}" +
                                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                            )
                        }
                    }
                }
            }
        } else UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
    }

    private fun initRegister() {
        googleLoginResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account.idToken)
                    } catch (e: ApiException) {
                        // Google 로그인 실패 처리
                        Log.d("google", "Google 로그인 실패: ${e.statusCode}")
                    }
                } else {
                    // 사용자가 로그인을 취소했거나 결과가 OK가 아닐 때의 처리
                    Log.d("오류", "로그인 사용자 취소 또는 실패: ${result.resultCode}")
                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val user = result.user
                Log.d("googleuser", "$user")
                // Handle successful sign in here
            }
            .addOnFailureListener { e ->
                Log.e("googleuser", "Google sign in failed", e)
                // Handle failure here
            }
    }

    private fun signOut() {
        // Firebase sign out
        firebaseAuth.signOut()

        // Google sign out
        googleSighInClient.signOut().addOnCompleteListener(requireActivity()) {
            Toast.makeText(requireContext(), "Complete", Toast.LENGTH_LONG).show()
        }
    }

    private fun revokeAccess() {
        // Firebase sign out
        firebaseAuth.signOut()

        // Google revoke access
        googleSighInClient.revokeAccess().addOnCompleteListener(requireActivity()) {
            Toast.makeText(requireContext(), "Complete", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = AuthenticationFragment()
    }
}