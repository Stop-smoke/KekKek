package com.stopsmoke.kekkek

import com.stopsmoke.kekkek.presentation.bookmark.BookmarkWritingItem
import com.stopsmoke.kekkek.presentation.community.CommunityListItem
import com.stopsmoke.kekkek.presentation.community.PostInfo
import com.stopsmoke.kekkek.presentation.community.UserInfo
import com.stopsmoke.kekkek.presentation.my.MyItem
import com.stopsmoke.kekkek.presentation.my.MyWritingNum
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

object DummyData{
    val CommunityList: List<CommunityListItem> = listOf(
        CommunityListItem.CommunityPopularItem(
            postInfo1 = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postInfo2 = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            )
        ),
        CommunityListItem.CommunityCategoryItem(
            listOf("전체 게시판", "인기 게시판", "자유 게시판", "금연 성공 후기", "금연 보조제 후기")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        CommunityListItem.CommunityWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        )
    )

    val myItem: MyItem = MyItem (
        name = "양동원",
        rank = 1,
        profileImg = "",
        myWriting=  MyWritingNum(3,2,1),
        achievementNum = 83,
        id = UUID.randomUUID().toString()
    )

    val bookmarkList: List<BookmarkWritingItem> = listOf(
        BookmarkWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        BookmarkWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        BookmarkWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ), BookmarkWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ), BookmarkWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
        BookmarkWritingItem(
            userInfo = UserInfo(
                name = "양동원",
                rank = 1,
                profileImage = ""
            ),
            postInfo = PostInfo(
                title="인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다.",
                postType = "자유게시판",
                view = 22222,
                like = 22222,
                comment = 11111
            ),
            postImage = "",
            post = "인기 많은 사람들을 위한 비결이 궁금하신가요? 저도 궁금합니다. 너무너무너무 너어무 너무너무너무 너어무 너무너무너너무",
            postTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("2023-05-01 12:00:00")
        ),
    )
}