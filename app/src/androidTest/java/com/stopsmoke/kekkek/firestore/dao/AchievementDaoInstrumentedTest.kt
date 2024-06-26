package com.stopsmoke.kekkek.firestore.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.data.AchievementDaoImpl
import com.stopsmoke.kekkek.firestore.model.AchievementEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AchievementDaoInstrumentedTest {
    private lateinit var achievementDao: AchievementDao

    @Before
    fun init() {
        achievementDao = AchievementDaoImpl(Firebase.firestore)
    }

    @Test
    fun generateAchievementItems() = runTest {
        generateCommentAchievement().forEach {
            achievementDao.addAchievementItem(it)
        }

        generatePostAchievement().forEach {
            achievementDao.addAchievementItem(it)
        }

        generateRankAchievement().forEach {
            achievementDao.addAchievementItem(it)
        }

        generateUserAchievement().forEach {
            achievementDao.addAchievementItem(it)
        }
    }


    private fun generateCommentAchievement(): List<AchievementEntity> {
        val category = "comment"
        val commentList = listOf(
            AchievementEntity(
                requestCode = "comment1",
                name = "오늘부터 키보드워리어",
                description = "댓글을 달기 시작했습니다.\n댓글 1개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 1
            ),
            AchievementEntity(
                requestCode = "comment5",
                name = "선한 영향력 행사중입니다.",
                description = "선한 영향력 행사중입니다.\n댓글 5개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 5
            ),
            AchievementEntity(
                requestCode = "comment10",
                name = "이 댓글은 영국에서 최초로 시작되어..\n",
                description = "지금 당신에게로 옮겨진 이 댓글은 4일 안에 당신 곁을 떠나야...\n댓글 10개를 달았습니다",
                image = "",
                category = category,
                maxProgress = 10
            ),
            AchievementEntity(
                requestCode = "comment30",
                name = "댓글 고수",
                description = "댓글로 무엇이든 벨 수 있을 것만 같습니다.\n댓글 30개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 30
            ),
            AchievementEntity(
                requestCode = "comment50",
                name = "댓글 초고수",
                description = "댓글로 무엇이든 벨 수 있습니다.\n댓글 50개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 50
            ),
            AchievementEntity(
                requestCode = "comment100",
                name = "다 덤벼! 나 댓글 내공 100이야!",
                description = "댓글 내공으로 장풍을 쏩니다.\n댓글 100개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 100
            ),
            AchievementEntity(
                requestCode = "comment150",
                name = "조심해! 이 앞은 낭떠러지야!",
                description = "앞으로 업적 달성이 더욱 어려워질겁니다.\n댓글 150개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 150
            ),
            AchievementEntity(
                requestCode = "comment300",
                name = "내가 바로 명품조연",
                description = "게시글에 당신이 자주 보입니다.\n댓글 300개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 300
            ),
            AchievementEntity(
                requestCode = "comment500",
                name = "이 곳의 마당발",
                description = "당신을 아는 사람이 많습니다.\n댓글 500개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 500
            ),
            AchievementEntity(
                requestCode = "comment750",
                name = "너 나 알아? 나 이런사람이야",
                description = "당신을 모르면 댓글을 본다고 할 수 없습니다.\n댓글 750개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 750
            ),
            AchievementEntity(
                requestCode = "comment1000",
                name = "댓글 천개에 소원을 빌어보세요.",
                description = "종이학 천개의 소원을 아시나요. 소원이 이루어지길.\n댓글 1000개를 달았습니다.",
                image = "",
                category = category,
                maxProgress = 1000
            )
        )
        return commentList
    }


    private fun generatePostAchievement(): List<AchievementEntity> {
        val category = "post"
        val postList = listOf(
            AchievementEntity(
                requestCode = "post1",
                name = "새싹 블로거",
                description = "자라나기 시작했습니다.\n게시글 1개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 1
            ),
            AchievementEntity(
                requestCode = "post5",
                name = "활발한 참여자",
                description = "운영자가 이 사용자를 좋아하기 시작합니다.\n게시글 5개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 5
            ),
            AchievementEntity(
                requestCode = "post10",
                name = "성장하는 블로거",
                description = "당신을 새싹이라 부르는 사람은 더이상 없습니다.\n게시글 10개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 10
            ),
            AchievementEntity(
                requestCode = "post30",
                name = "참여상 수여",
                description = "위 사람은 커뮤니티 참여로 상장을 수여함.\n게시글 30개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 30
            ),
            AchievementEntity(
                requestCode = "post50",
                name = "절반밖에? 절반이나!",
                description = "긍정적인 생각은 당신을 긍정적인 삶으로 이끌어줄지도?\n게시글 50개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 50
            ),
            AchievementEntity(
                requestCode = "post100",
                name = "감사합니다! 사랑합니다!",
                description = "활발히 참여해주셔서 정말 감사합니다! 사랑합니다!\n게시글 100개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 100
            ),
            AchievementEntity(
                requestCode = "post150",
                name = "50보100보 총 150보?",
                description = "백오십백오십백십오십백? 헛소리 죄송합니다.\n게시글 150개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 150
            ),
            AchievementEntity(
                requestCode = "post300",
                name = "정예 포스트 300",
                description = "스파르타 300같은 정예가 당신입니다.\n게시글 300개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 300
            ),
            AchievementEntity(
                requestCode = "post500",
                name = "우리 거의 가족인데?",
                description = "삶의 일부를 공유한 우리는 거의 가족이네요.\n게시글 500개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 500
            ),
            AchievementEntity(
                requestCode = "post750",
                name = "커뮤니티 네임드",
                description = "커뮤니티를 활동하는 사람들은 모두 당신의 이름을 알 것입니다.\n게시글 750개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 750
            ),
            AchievementEntity(
                requestCode = "post1000",
                name = "커뮤니티 터줏대감",
                description = "몇 몇 사람은 커뮤니티를 시작할 때 당신에게 인사할지도?\n게시글 1000개를 작성했습니다.",
                image = "",
                category = category,
                maxProgress = 1000
            )
        )
        return postList
    }

    private fun generateUserAchievement(): List<AchievementEntity> {
        val category = "user"
        val userList = listOf(
            AchievementEntity(
                requestCode = "time1",
                name = "시작은 미약하나 그 끝은 창대하리라",
                description = "처음엔 몰라도 나중가면 엄청난 이득이 생길겁니다.\n금연 1일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 1
            ),
            AchievementEntity(
                requestCode = "time5",
                name = "자라나라 수명수명",
                description = "수명이 자라나고 있습니다.\n금연 5일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 5
            ),
            AchievementEntity(
                requestCode = "time10",
                name = "금연하지마. 동작그만! 손모가지 날라가붕게",
                description = "담배핀건 아니시죠?\n금연 10일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 10
            ),
            AchievementEntity(
                requestCode = "time30",
                name = "오늘이 무슨날이게?",
                description = "금연 30일이야.\n금연 30일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 30
            ),
            AchievementEntity(
                requestCode = "time50",
                name = "어떻게 금연까지 사랑하겠어",
                description = "금연하는 날 좋아하는 널 사랑하는거지.\n금연 50일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 50
            ),
            AchievementEntity(
                requestCode = "time100",
                name = "우리 오래오래 건강하자",
                description = "당신을 보는 개발자가 뿌듯해합니다.\n금연 100일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 100
            ),
            AchievementEntity(
                requestCode = "time150",
                name = "거의 반년차 금연자",
                description = "금연 반년했다고 자랑하고 다니세요. 거의 비슷해요.\n금연 150일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 150
            ),
            AchievementEntity(
                requestCode = "time300",
                name = "300만큼 건강해",
                description = "3000만큼은 아닌 게 아쉽네요. 3000까지 달려갑시다.\n금연 300일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 300
            ),
            AchievementEntity(
                requestCode = "time500",
                name = "오백일인데 우리 결혼할까?",
                description = "백년해로하자. 건강하게 살게 해줄게!\n금연 500일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 500
            ),
            AchievementEntity(
                requestCode = "time750",
                name = "힘든 일도 많겠지만 힘내자!",
                description = "내일은 오늘보다 나은 삶이 있을 겁니다.\n금연 750일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 750
            ),
            AchievementEntity(
                requestCode = "time1000",
                name = "천일의 법칙",
                description = "설정한 목표를 향해 꾸준히 나아가는 당신은 성공한 사람입니다.\n금연 1000일을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 1000
            )
        )
        return userList
    }

    private fun generateRankAchievement(): List<AchievementEntity> {
        val category = "rank"
        val rankList = listOf(
            AchievementEntity(
                requestCode = "rank1000",
                name = "등반 시작",
                description = "랭킹을 올리기 시작했습니다.\n랭킹 1000을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 1000
            ),
            AchievementEntity(
                requestCode = "rank500",
                name = "시작이 반이다.",
                description = "랭킹 500을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 500
            ),
            AchievementEntity(
                requestCode = "rank100",
                name = "백..,백?,백!",
                description = "종이백, 가방백, 핸드백.\n랭킹 100을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 100
            ),
            AchievementEntity(
                requestCode = "rank10",
                name = "탑 랭커",
                description = "열 손가락 안에 들었습니다.\n랭킹 10등을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 10
            ),
            AchievementEntity(
                requestCode = "rank5",
                name = "국가권력급 금연자",
                description = "어디 가서 자랑하고 다니셔도 됩니다.\n랭킹 5등을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 5
            ),
            AchievementEntity(
                requestCode = "rank1",
                name = "당신은 전설이 되었습니다.",
                description = "당신의 전설은 길이 화자될 것입니다. 미쳐 날뛰고 있습니다!\n랭킹 1등을 달성했습니다.",
                image = "",
                category = category,
                maxProgress = 1
            )
        )
        return rankList
    }


}