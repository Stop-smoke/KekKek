package com.agvber.kekkek.core.domain.model

/**
 * [Me] 내가 작성한 댓글 목록을 반환 합니다
 *
 * [Post] 조회 하고 싶은 게시글 댓글 목록을 반환 합니다
 */
sealed interface CommentFilter {

    data class Post(val postId: String) : CommentFilter

    data class User(val uid: String) : CommentFilter

}