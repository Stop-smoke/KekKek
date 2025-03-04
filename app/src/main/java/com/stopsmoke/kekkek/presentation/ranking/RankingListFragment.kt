package com.stopsmoke.kekkek.presentation.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentRankingListBinding
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.home.HomeUiState
import com.stopsmoke.kekkek.presentation.home.HomeViewModel
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.ranking.field.RankingListField
import com.stopsmoke.kekkek.presentation.ranking.field.RankingListFieldFragment
import com.stopsmoke.kekkek.presentation.userprofile.navigateToUserProfileScreen
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RankingListFragment : Fragment(), RankingListCallback, ErrorHandle {
    private var _binding: FragmentRankingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        setBackPressed()
        replaceFragment(RankingListFieldFragment.newInstance(RankingListField.Time))
    }

    private fun initViewModel() = with(viewModel){
        viewLifecycleOwner.lifecycleScope.launch {
            homeUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    when (state) {
                        is HomeUiState.ErrorExit -> {
                            errorExit(findNavController())
                        }
                        else -> {}
                    }
                }
        }
    }
    private fun setBackPressed() {
        val ivRankingListBack = binding.includeRankingListAppBar.ivRankingListBack
        ivRankingListBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        activity?.visible()
    }


    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayout_rankingList, fragment)
            .commit()
    }

    override fun navigationToUserProfile(uid: String) {
        findNavController().navigateToUserProfileScreen(uid)
    }
}
