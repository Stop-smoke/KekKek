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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.oAuthCredential
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.stopsmoke.kekkek.databinding.FragmentAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding get() = _binding!!

    private lateinit var googleSighInClient: GoogleSignInClient
    private lateinit var googleLoginResult: ActivityResultLauncher<Intent>

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        registerGoogleAuthRequest()
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

        binding.ivAuthKakaoLogin.setOnClickListener {
            loginKakao()
        }

        binding.ivAuthGoogleLogin.setOnClickListener {
            clickGoogleLoginButton()
        }
    }

    private fun registerGoogleAuthRequest() {
        googleLoginResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Google sign in failed", e)
                        e.printStackTrace()
                    }
                }
            }
    }

    private fun clickGoogleLoginButton() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIRBASE_AUTH_SERVCER_CLIENT_KEY)
            .requestId()
            .requestEmail()
            .requestProfile()
            .build()

        googleSighInClient = GoogleSignIn.getClient(requireContext(), options)
        googleLoginResult.launch(googleSighInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("Google 로그인", user.toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun loginKakao() {
        if (!getKakaoAppLoginAvailable()) {
            loginKakaoWeb()
            return
        }
        UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
            if (error != null || token == null) {
                Log.e("kakao", "카카오톡으로 로그인 실패", error)
                loginKakaoWeb()
                return@loginWithKakaoTalk
            }

            UserApiClient.instance.me { user, error ->
                if (error != null || user == null) {
                    Log.e("kakaouser", "사용자 정보 요청 실패", error)
                    return@me
                }
                token.registerKakaoToken()
                showSuccessLog(user)
            }
            Log.i("kakao", "카카오톡으로 로그인 성공 ${token.accessToken}")
        }
    }

    private fun getKakaoAppLoginAvailable() =
        UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())

    private fun loginKakaoWeb() {
        UserApiClient.instance.loginWithKakaoAccount(
            context = requireContext(),
            callback = { token, error ->
                if (error != null) {
                    Log.e("kakao", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    token.registerKakaoToken()
                    Log.i("kakao", "카카오계정으로 로그인 성공 ${token.accessToken}")
                }
            }
        )
    }

    private fun showSuccessLog(
        user: com.kakao.sdk.user.model.User,
    ) {
        Log.i(
            "kakaouser", "사용자 정보 요청 성공" +
                    "\n회원번호: ${user.id}" +
                    "\n이메일: ${user.kakaoAccount?.email}" +
                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
        )
    }

    private fun OAuthToken.registerKakaoToken() {
        val providerId = "oidc.kakao"
        val credential = oAuthCredential(providerId) {
            idToken = this@registerKakaoToken.idToken
            accessToken = this@registerKakaoToken.accessToken
        }

        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                authResult.user?.displayName?.let {
                    Toast.makeText(requireContext(), "${it}님 환영합니다", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "로그인을 실패하였습니다", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = AuthenticationFragment()

        private const val TAG = "AuthenticationFragment"
    }
}