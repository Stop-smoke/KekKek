package com.stopsmoke.kekkek.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.databinding.FragmentNotificationBinding
import com.stopsmoke.kekkek.presentation.notification.recyclerview.adapter.NotificationItemListAdapter
import com.stopsmoke.kekkek.presentation.notification.recyclerview.decoration.NotificationDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAppBar()
        initMainRecyclerview()
        observeRecyclerViewItem()
    }

    private fun initAppBar() = with(binding) {
        clNotificationAppBar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initMainRecyclerview() {
        binding.rvNotificationItem.adapter = NotificationItemListAdapter()
        binding.rvNotificationItem.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotificationItem.addItemDecoration(
            NotificationDividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    private fun observeRecyclerViewItem() {
        lifecycleScope.launch {
            viewModel.notificationItem?.collect {
                (binding.rvNotificationItem.adapter as NotificationItemListAdapter)
                    .submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = NotificationFragment()
    }
}