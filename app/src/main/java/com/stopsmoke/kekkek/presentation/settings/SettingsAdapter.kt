package com.stopsmoke.kekkek.presentation.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.databinding.ItemSettingsListBinding
import com.stopsmoke.kekkek.databinding.ItemSettingsProfileBinding
import com.stopsmoke.kekkek.databinding.ItemSettingsVersionBinding
import com.stopsmoke.kekkek.databinding.UnknownItemBinding
import com.stopsmoke.kekkek.presentation.settings.model.SettingsMultiViewEnum
import com.stopsmoke.kekkek.presentation.settings.model.SettingsOnClickListener
import com.stopsmoke.kekkek.presentation.settings.model.SettingsItem

class SettingsAdapter(private val settingsOnClickListener: SettingsOnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = listOf<SettingsItem>()

    class UserProfileViewHolder(private val binding: ItemSettingsProfileBinding) : //ItemSettingsProfileBinding
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: SettingsItem, settingsOnClickListener: SettingsOnClickListener) {
            binding.run {
                tvSettingUsername.text = setting.profileInfo?.userNickname
                circleIvSettingProfile.load(setting.profileInfo?.profileImg) {
                    crossfade(true)
                }
                root.setOnClickListener {
                    settingsOnClickListener.onClickProfile(setting)
                }
            }
        }
    }

    class SettingListViewHolder(private val binding: ItemSettingsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: SettingsItem, settingsOnClickListener: SettingsOnClickListener) = with(binding) {
            tvSettingListName.text = setting.settingTitle
            if(setting.settingTitle == "알림") {
                ivSettingListShowMore.visibility = View.GONE
                tvSettingNotification.visibility = View.VISIBLE
                val isNotificationActive =
                    NotificationManagerCompat.from(root.context).areNotificationsEnabled()
                if(isNotificationActive) {
                    tvSettingNotification.text = "켜짐"
                } else {
                    tvSettingNotification.text = "꺼짐"
                }
            }
            root.setOnClickListener {
                settingsOnClickListener.onClickSettingList(setting)
            }
        }
    }

    class SettingVersionViewHolder(private val binding: ItemSettingsVersionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: SettingsItem) = with(binding) {
            tvSettingVersion.text = setting.version
        }
    }

    class UnknownViewHolder(private val binding: UnknownItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() = Unit
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val multiViewType = SettingsMultiViewEnum.entries.find {
            it.viewType == viewType
        }

        return when (multiViewType) {
            SettingsMultiViewEnum.MY_PAGE -> {
                val binding = ItemSettingsProfileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserProfileViewHolder(binding)
            }

            SettingsMultiViewEnum.LIST -> {
                val binding = ItemSettingsListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingListViewHolder(binding)
            }

            SettingsMultiViewEnum.VERSION -> {
                val binding = ItemSettingsVersionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingVersionViewHolder(binding)
            }

            else -> {
                val binding =
                    UnknownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UnknownViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = itemList[position]
        when (holder.itemViewType) {
            SettingsMultiViewEnum.MY_PAGE.viewType -> {
                val profileHolder = holder as UserProfileViewHolder
                profileHolder.bind(currentItem, settingsOnClickListener)
            }

            SettingsMultiViewEnum.LIST.viewType -> {
                val listHolder = holder as SettingListViewHolder
                listHolder.bind(currentItem, settingsOnClickListener)
            }

            SettingsMultiViewEnum.VERSION.viewType -> {
                val versionHolder = holder as SettingVersionViewHolder
                versionHolder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].cardViewType.viewType
    }

}