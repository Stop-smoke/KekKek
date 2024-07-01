package com.stopsmoke.kekkek.presentation.post.reply

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.databinding.ItemReplyBinding
import com.stopsmoke.kekkek.databinding.UnknownItemBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.post.PostCommentCallback
import com.stopsmoke.kekkek.presentation.toResourceId

class ReplyAdapter(
    private val viewModel: ReplyViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
) : PagingDataAdapter<Reply, ReplyAdapter.ViewHolder>(diffCallback) {

    private var callback: ReplyCallback? = null

    fun registerCallback(replyCallback: ReplyCallback) {
        callback = replyCallback
    }

    fun unregisterCallback() {
        callback = null
    }


    fun updateComment() {
        notifyDataSetChanged()
    }


    abstract class ViewHolder(
        root: View
    ) : RecyclerView.ViewHolder(root) {
        abstract fun bind(reply: Reply)
    }

    enum class ViewType {
        COMMENT, REPLY
    }

    inner class CommentViewHolder(
        private val binding: ItemCommentBinding
    ) : ViewHolder(binding.root) {
        override fun bind(reply: Reply):Unit = with(binding) {
            val comment = viewModel.comment.value ?: return@with
            tvCommentNickname.text = comment.written.name
            tvCommentDescription.text = comment.text
            tvCommentHour.text = comment.elapsedCreatedDateTime.toResourceId(itemView.context)

            comment.written.profileImage.let { profileImage ->
                when (profileImage) {
                    is ProfileImage.Web -> ivCommentProfile.load(profileImage.url)
                    is ProfileImage.Default -> ivCommentProfile.setImageResource(R.drawable.ic_user_profile_test)
                }
            }

            tvCommentLikeNum.text = comment.likeUser.size.toString()
            val userUid = (viewModel.user.value as? User.Registered)?.uid ?: ""
            var isLikeUser: Boolean = userUid in viewModel.comment.value!!.likeUser

            if (isLikeUser) ivCommentLike.setColorFilter(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.primary_blue
                )
            )
            else ivCommentLike.setColorFilter(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.gray_lightgray2
                )
            )

            ivCommentProfile.setOnClickListener {
                callback?.navigateToUserProfile(comment.written.uid)
            }

            clCommentLike.setOnClickListener {
                val list = comment.likeUser.toMutableList()
                if (isLikeUser) list.remove(userUid) else list.add(userUid)
                callback?.commentLikeClick(
                    comment.copy(
                        likeUser = list
                    )
                )
            }
        }
    }

    inner class ReplyViewHolder(
        private val binding: ItemReplyBinding
    ) : ViewHolder(binding.root) {
        override fun bind(reply: Reply) = with(binding) {
            tvCommentNickname.text = reply.written.name
            tvCommentDescription.text = reply.text
            tvCommentHour.text = reply.elapsedCreatedDateTime.toResourceId(itemView.context)

            reply.written.profileImage.let { profileImage ->
                when (profileImage) {
                    is ProfileImage.Web -> ivCommentProfile.load(profileImage.url)
                    is ProfileImage.Default -> ivCommentProfile.setImageResource(R.drawable.ic_user_profile_test)
                }
            }

            tvCommentLikeNum.text = reply.likeUser.size.toString()
            val userUid = (viewModel.user.value as? User.Registered)?.uid ?: ""
            val isLikeUser: Boolean = userUid in reply.likeUser
            if (isLikeUser) ivCommentLike.setColorFilter(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.primary_blue
                )
            )
            else ivCommentLike.setColorFilter(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.gray_lightgray2
                )
            )

            ivCommentProfile.setOnClickListener {
                callback?.navigateToUserProfile(reply.written.uid)
            }


            clCommentLike.setOnClickListener {
                val list = reply.likeUser.toMutableList()
                if (isLikeUser) list.remove(userUid) else list.add(userUid)
                callback?.updateReply(
                    reply.copy(
                        likeUser = list
                    )
                )
            }

            itemView.setOnLongClickListener {
                callback?.deleteItem(reply)
                true
            }
        }

    }

    class UnknownViewHolder(
        binding: UnknownItemBinding
    ) : ViewHolder(binding.root) {
        override fun bind(reply: Reply) {}
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ViewType.COMMENT.ordinal
        else ViewType.REPLY.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            ViewType.COMMENT.ordinal -> CommentViewHolder(
                ItemCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ViewType.REPLY.ordinal -> ReplyViewHolder(
                ItemReplyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> UnknownViewHolder(
                UnknownItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Reply>() {
            override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
                return oldItem.id == newItem.id

            }
        }
    }
}