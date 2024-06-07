package com.stopsmoke.kekkek.presentation.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.databinding.ItemSettingListBinding
import com.stopsmoke.kekkek.databinding.ItemSettingProfileBinding
import com.stopsmoke.kekkek.databinding.ItemSettingVersionBinding
import com.stopsmoke.kekkek.databinding.UnknownItemBinding

class SettingAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = listOf<SettingItem>()

    class UserProfileViewHolder(private val binding: ItemSettingProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: SettingItem, onClickListener: OnClickListener) {
            binding.run {
                tvSettingUsername.text = setting.profileInfo?.userNickname
                circleIvSettingProfile.load(setting.profileInfo?.profileImg) {
                    crossfade(true)
                }
                root.setOnClickListener {
                    onClickListener.onClickProfile(setting)
                }
            }
        }
    }

    class SettingListViewHolder(private val binding: ItemSettingListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: SettingItem, onClickListener: OnClickListener) = with(binding) {
            tvSettingListName.text = setting.settingTitle
            root.setOnClickListener {
                onClickListener.onClickSettingList(setting)
            }
        }
    }

    class SettingVersionViewHolder(private val binding: ItemSettingVersionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: SettingItem) = with(binding) {
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
        val multiViewType = MultiViewEnum.entries.find {
            it.viewType == viewType
        }

        return when (multiViewType) {
            MultiViewEnum.MY_PAGE -> {
                val binding = ItemSettingProfileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserProfileViewHolder(binding)
            }

            MultiViewEnum.LIST -> {
                val binding = ItemSettingListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingListViewHolder(binding)
            }

            MultiViewEnum.VERSION -> {
                val binding = ItemSettingVersionBinding.inflate(
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
            MultiViewEnum.MY_PAGE.viewType -> {
                val profileHolder = holder as UserProfileViewHolder
                profileHolder.bind(currentItem, onClickListener)
            }

            MultiViewEnum.LIST.viewType -> {
                val listHolder = holder as SettingListViewHolder
                listHolder.bind(currentItem, onClickListener)
            }

            MultiViewEnum.VERSION.viewType -> {
                val versionHolder = holder as SettingVersionViewHolder
                versionHolder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].cardViewType.viewType
    }

}