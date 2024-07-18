package com.stopsmoke.kekkek.presentation.post.popular

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.core.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.core.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.getRelativeTime

class PopularPostListViewHolder(
    private val binding: ItemPostBinding,
    private val callback: CommunityCallbackListener?,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CommunityWritingItem) = with(binding) {
        item.postInfo.let {
            tvItemWritingTitle.text = it.title
            tvItemWritingViewNum.text = it.view.toString()
            tvItemWritingLikeNum.text = it.like.toString()
            tvItemWritingCommentNum.text = it.comment.toString()
        }

        tvItemWritingPost.text = item.post
        tvItemWritingTimeStamp.text = getRelativeTime(item.postTime)

        item.userInfo.let {
            if (item.postImage != "") {
                ivItemWritingPostImage.load(it.profileImage) {
                    crossfade(true)
//                    placeholder(R.drawable.placeholder) 로딩중 띄우나?
//                    error(R.drawable.error) 오류시 띄우나?
                }
            } else {
                ivItemWritingPostImage.visibility = View.GONE
                setMarginEnd(tvItemWritingTitle, 16)
            }

            tvItemWritingName.text = it.name
            tvItemWritingRank.text = "랭킹 ${it.rank}위"

            circleIvItemWritingProfile.load(it.profileImage)
        }

        tvItemWritingPostType.text = when (item.postType) {
            PostCategory.NOTICE -> "공지사항"
            PostCategory.QUIT_SMOKING_SUPPORT -> "금연 지원 프로그램 공지"
            PostCategory.POPULAR -> "인기글"
            PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "금연 보조제 후기"
            PostCategory.SUCCESS_STORIES -> "금연 성공 후기"
            PostCategory.GENERAL_DISCUSSION -> "자유 게시판"
            PostCategory.FAILURE_STORIES -> "금연 실패 후기"
            PostCategory.RESOLUTIONS -> "금연 다짐"
            PostCategory.UNKNOWN -> ""
            PostCategory.ALL -> ""
        }


        binding.circleIvItemWritingProfile.setOnClickListener {
            callback?.navigateToUserProfile(item.userInfo.uid)
        }

        binding.root.setOnClickListener {
            callback?.navigateToPost(item.postInfo.id)
        }
    }



    private fun setMarginEnd(view: TextView, end: Int) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginEnd = end
        view.layoutParams = layoutParams
    }
}