package com.stopsmoke.kekkek.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.databinding.FragmentAuthenticationBinding
import com.stopsmoke.kekkek.presentation.authentication.dialog.TermBottomSheetDialog
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.home.navigateToHomeScreenWithClearBackStack
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.onboarding.model.AuthenticationEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding get() = _binding!!

    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.invisible()
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
        handleAppEvent()

        binding.flLoginKakao.setOnClickListener {
            viewModel.loginKakao()
        }

        binding.flLoginGoogle.setOnClickListener {
            viewModel.loginGoogle()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleAppEvent() = lifecycleScope.launch {
        viewModel.event.collectLatestWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = Lifecycle.State.STARTED
        ) { uiEvent ->
            when (uiEvent) {
                is AuthenticationEvent.AlreadyUser -> {
                    findNavController().navigateToHomeScreenWithClearBackStack()
                }

                is AuthenticationEvent.NewMember -> {
                    val termDialog = TermBottomSheetDialog()
                    termDialog.show(childFragmentManager, termDialog.tag)
                }

                is AuthenticationEvent.Error -> {
                    Toast.makeText(requireContext(), "에러가 발생하였습니다", Toast.LENGTH_SHORT)
                        .show()
                }

                is AuthenticationEvent.Guest -> {}

                is AuthenticationEvent.Init -> {}
            }
        }
    }
}