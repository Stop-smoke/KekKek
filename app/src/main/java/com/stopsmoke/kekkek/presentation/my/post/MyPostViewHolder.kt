package com.stopsmoke.kekkek.presentation.my.post

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.toResourceId

class MyPostViewHolder(
    private val binding: ItemPostBinding,
    private val callback: CommunityCallbackListener?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Post) = with(binding) {

        tvItemWritingTitle.text = item.title
        tvItemWritingViewNum.text = item.views.toString()
        tvItemWritingLikeNum.text = item.likeUser.size.toString()
        tvItemWritingCommentNum.text = item.commentCount.toString()

        tvItemWritingPost.text = item.text
        tvItemWritingTimeStamp.text = item.createdElapsedDateTime.toResourceId(itemView.context)

        tvItemWritingName.text = item.written.name
        tvItemWritingRank.text = "랭킹 ${item.written.ranking}위"

        when (item.written.profileImage) {
            ProfileImage.Default -> {
                circleIvItemWritingProfile.setImageResource(R.drawable.ic_user_profile_test)
            }

            is ProfileImage.Web -> {
                circleIvItemWritingProfile.load(item.written.profileImage.url) {
                    crossfade(true)
                }
            }
        }

        binding.root.setOnClickListener {
            callback?.navigateToPost(item.id)
        }

//            item.userInfo.let {
//                if (item.postImage != "") {
//                    ivItemWritingPostImage.load(it.profileImage) {
//                        crossfade(true)
////                    placeholder(R.drawable.placeholder) 로딩중 띄우나?
////                    error(R.drawable.error) 오류시 띄우나?
//                    }
//                } else {
//                    ivItemWritingPostImage.visibility = View.GONE
//                    setMarginEnd(tvItemWritingTitle, 16)
//                }
//
//                tvItemWritingName.text = it.name
//                tvItemWritingRank.text = "랭킹 ${it.rank}위"
//            }

        tvItemWritingPostType.text = when (item.category) {
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
    }

    private fun getRelativeTime(pastTime: ElapsedDateTime): String {
        val timeType = when (pastTime.elapsedDateTime) {
            DateTimeUnit.YEAR -> "년"
            DateTimeUnit.MONTH -> "달"
            DateTimeUnit.DAY -> "일"
            DateTimeUnit.WEEK -> "주"
            DateTimeUnit.HOUR -> "시간"
            DateTimeUnit.MINUTE -> "분"
            DateTimeUnit.SECOND -> "초"
        }

        return "${pastTime.number} ${timeType} 전"
    }

    private fun setMarginEnd(view: TextView, end: Int) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginEnd = end
        view.layoutParams = layoutParams
    }
}