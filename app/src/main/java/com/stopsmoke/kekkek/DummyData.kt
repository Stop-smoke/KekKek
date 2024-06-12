package com.stopsmoke.kekkek

import com.stopsmoke.kekkek.firestore.model.AchievementEntity
import com.stopsmoke.kekkek.presentation.my.MyItem
import com.stopsmoke.kekkek.presentation.my.MyWritingNum
import java.util.Locale
import java.util.UUID

object DummyData{
    val myItem: MyItem = MyItem (
        name = "양동원",
        rank = 1,
        profileImg = "",
        myWriting=  MyWritingNum(3,2,1),
        achievementNum = 83,
        id = UUID.randomUUID().toString()
    )


    val category = "comment"
    val achievementItem: List<AchievementEntity> = listOf(
        AchievementEntity(
            id = "comment1",
            name = "오늘부터 키보드워리어",
            content = "댓글을 달기 시작했습니다.\n댓글 1개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 1
        ),
        AchievementEntity(
            id = "comment5",
            name = "선한 영향력 행사중입니다.",
            content = "선한 영향력 행사중입니다.\n댓글 5개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 5
        ),
        AchievementEntity(
            id = "comment10",
            name = "이 댓글은 영국에서 최초로 시작되어..\n",
            content = "지금 당신에게로 옮겨진 이 댓글은 4일 안에 당신 곁을 떠나야...\n댓글 10개를 달았습니다",
            image = "",
            category = category,
            maxProgress = 10
        ),
        AchievementEntity(
            id = "comment30",
            name = "댓글 고수",
            content = "댓글로 무엇이든 벨 수 있을 것만 같습니다.\n댓글 30개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 30
        ),
        AchievementEntity(
            id = "comment50",
            name = "댓글 초고수",
            content = "댓글로 무엇이든 벨 수 있습니다\n댓글 50개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 50
        ),
        AchievementEntity(
            id = "comment100",
            name = "다 덤벼! 나 댓글 내공 100이야!",
            content = "댓글내공으로 장풍을 쏩니다.\n댓글 100개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 100
        ),
        AchievementEntity(
            id = "comment150",
            name = "조심해! 이 앞은 낭떠러지야!",
            content = "앞으로 업적 달성이 더욱 어려워질겁니다.\n댓글 150개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 150
        ),
        AchievementEntity(
            id = "comment300",
            name = "내가 바로 명품조연",
            content = "게시글에 당신이 자주 보입니다.\n댓글 300개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 300
        ),
        AchievementEntity(
            id = "comment500",
            name = "이 곳의 마당발",
            content = "당신을 아는 사람이 많습니다.\n댓글 500개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 500
        ),
        AchievementEntity(
            id = "comment750",
            name = "너 나 알아? 나 이런사람이야",
            content = "당신을 모르면 댓글을 본다고 할 수 없습니다.\n댓글 750개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 750
        ),
        AchievementEntity(
            id = "comment1000",
            name = "댓글 천개에 소원을 빌어보세요.",
            content = "종이학 천개의 소원을 아시나요. 소원이 이루어지길.\n댓글 1000개를 달았습니다.",
            image = "",
            category = category,
            maxProgress = 1000
        )
    )
}