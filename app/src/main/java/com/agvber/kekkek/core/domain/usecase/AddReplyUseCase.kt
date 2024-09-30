package com.agvber.kekkek.core.domain.usecase

import com.agvber.kekkek.core.domain.model.CommentParent
import com.agvber.kekkek.core.domain.model.DateTime
import com.agvber.kekkek.core.domain.model.Reply
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.model.Written
import com.agvber.kekkek.core.domain.repository.ReplyRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import javax.inject.Inject

class AddReplyUseCase @Inject constructor(
    private val replyRepository: ReplyRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        post: CommentParent,
        commentId: String,
        text: String,
    ) : String{
        val user = userRepository.getUserData().first() as User.Registered

        return replyRepository.addReply(
            reply = Reply(
                id = "",
                written = Written(
                    uid = user.uid,
                    name = user.name,
                    profileImage = user.profileImage,
                    ranking = user.ranking
                ),
                likeUser = emptyList(),
                unlikeUser = emptyList(),
                dateTime = LocalDateTime.now().let { DateTime(it, it) },
                text = text,
                commentParent = post,
                replyParent = commentId,
                isLiked = false
            )
        )
    }
}