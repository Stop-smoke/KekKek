package com.stopsmoke.kekkek.presentation.post.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentNoticeListBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.isVisible
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class NoticeListFragment : Fragment() {
    private var _binding: FragmentNoticeListBinding? = null
    val binding: FragmentNoticeListBinding get() = _binding!!

    private val listAdapter: NoticeListAdapter by lazy {
        NoticeListAdapter()
    }

    private val viewModel: NoticeListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        initAppBar()
        rvNoticeList.adapter = listAdapter
        rvNoticeList.layoutManager = LinearLayoutManager(requireActivity())

        initListAdapterCallback()
    }

    private fun initAppBar() = with(binding) {
        val ivNoticeListBack =
            requireActivity().findViewById<ImageView>(R.id.iv_notification_back)
        val ivNoticeListDelete =
            requireActivity().findViewById<ImageView>(R.id.iv_notification_delete)
        val tvNoticeListTitle =
            requireActivity().findViewById<TextView>(R.id.tv_notification_title)

        ivNoticeListBack.setOnClickListener {
            findNavController().popBackStack()
        }

        ivNoticeListDelete.visibility = View.GONE

        tvNoticeListTitle.text = "공지사항"
    }

    private fun initListAdapterCallback() {
        listAdapter.registerCallbackListener(
            object : CommunityCallbackListener {
                override fun navigateToUserProfile(uid: String) {}
                override fun navigateToPost(postId: String) {
                    findNavController().navigate(
                        resId = R.id.action_noticeList_to_postView,
                        args = bundleOf("post_id" to postId)
                    )
                }
            }
        )
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            noticePosts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { noticePosts ->
                    listAdapter.submitData(noticePosts)
                }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        listAdapter.unregisterCallbackListener()
        _binding = null
        activity?.visible()
    }

    override fun onResume() {
        super.onResume()
        activity?.let { activity ->
            if (activity.isVisible()) {
                listAdapter.refresh()
            }
            activity.invisible()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoticeListFragment()
    }
}