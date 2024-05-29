package com.stopsmoke.kekkek

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kakao.sdk.user.UserApiClient
import com.stopsmoke.kekkek.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAuthKakaoLogin.setOnClickListener {
            clickKakaoLoginButton()
        }
    }

    private fun clickKakaoLoginButton() {
        UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
            if (error != null) {
                Log.e("TAG", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("TAG", "로그인 성공 ${token.accessToken}, id: ${token.idToken}")
            }
        }

        UserApiClient.instance.me { user, error ->
            user?.id?.let {
                Log.d("TEST", it.toString())
            }
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