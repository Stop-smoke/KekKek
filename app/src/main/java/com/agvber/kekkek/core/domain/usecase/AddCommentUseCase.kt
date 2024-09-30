package com.agvber.kekkek.core.domain.usecase

import com.agvber.kekkek.core.domain.model.Comment
import com.agvber.kekkek.core.domain.model.CommentParent
import com.agvber.kekkek.core.domain.model.DateTime
import com.agvber.kekkek.core.domain.model.PostCategory
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.model.Written
import com.agvber.kekkek.core.domain.repository.CommentRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        postId: String,
        postTitle: String,
        postType: PostCategory,
        text: String,
    ): String{
        val user = userRepository.getUserData().first() as User.Registered

        val comment = Comment(
            id = "",
            text = text,
            dateTime = LocalDateTime.now().let {
                DateTime(
                    it,
                    it
                )
            },
            likeUser = emptyList(),
            unlikeUser = emptyList(),
            earliestReply = emptyList(),
            written = Written(
                uid = user.uid,
                name = user.name,
                profileImage = user.profileImage,
                ranking = user.ranking,

                ),
            parent = CommentParent(
                postType = postType, postId = postId, postTitle = postTitle
            ),
            replyCount = 0,
            isLiked = false
        )
        return commentRepository.addCommentItem(comment)
    }
}