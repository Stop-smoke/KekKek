package com.stopsmoke.kekkek.domain.usecase

import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentParent
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.Written
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
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
            dateTime = LocalDateTime.now().let { DateTime(it, it) },
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
            replyCount = 0
        )
        return commentRepository.addCommentItem(comment)
    }
}