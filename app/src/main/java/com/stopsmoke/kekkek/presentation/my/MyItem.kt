package com.stopsmoke.kekkek.presentation.my

data class MyItem(
    val name: String,
    val rank: Long,
    val profileImg: String,
    val myWriting: MyWritingNum,
    val achievementNum: Int,
    val id: String // id로 내가 쓴 글, 업적 데이터 조회?
)

data class MyWritingNum(
    val writing: Int,
    val comment: Int,
    val bookmark: Int
)