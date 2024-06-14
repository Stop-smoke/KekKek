package com.stopsmoke.kekkek.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.firestore.dao.PostDao
import com.stopsmoke.kekkek.firestore.data.pager.FireStorePagingSource
import com.stopsmoke.kekkek.firestore.model.PostEntity
import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

internal class PostDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : PostDao {

    override fun getPost(category: String?): Flow<PagingData<PostEntity>> {
        val query = firestore.collection(COLLECTION)
            .whereNotNullEqualTo("category", category)
            .orderBy("date_time", Query.Direction.DESCENDING)


        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = PostEntity::class.java
            )

        }.flow
    }

    override fun getPostForWrittenUid(writtenUid: String): Flow<PagingData<PostEntity>> {
        return try {
            val query = firestore.collection(COLLECTION)
                .whereEqualTo("written.uid", writtenUid)
                .orderBy("date_time", Query.Direction.DESCENDING)
                .limit(10)

            return Pager(
                config = PagingConfig(PAGE_LIMIT)
            ) {
                FireStorePagingSource(
                    query = query,
                    limit = PAGE_LIMIT.toLong(),
                    clazz = PostEntity::class.java
                )

            }.flow
        } catch (e: Exception) {
            e.printStackTrace()
            //빈 페이저
            Pager(
                config = PagingConfig(PAGE_LIMIT)
            ) {
                object : PagingSource<Int, PostEntity>() {
                    override fun getRefreshKey(state: PagingState<Int, PostEntity>): Int? = null

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostEntity> {
                        return LoadResult.Page(
                            data = emptyList(),
                            prevKey = null,
                            nextKey = null
                        )
                    }
                }
            }.flow
        }
    }

    override fun getBookmark(postIdList: List<String>): Flow<PagingData<PostEntity>> {
        return try {
            val query = firestore.collection(COLLECTION)
                .whereIn("id", postIdList)

            Pager(
                config = PagingConfig(PAGE_LIMIT)
            ) {
                FireStorePagingSource(
                    query = query,
                    limit = PAGE_LIMIT.toLong(),
                    clazz = PostEntity::class.java
                )

            }.flow
        } catch (e: Exception) {
            e.printStackTrace()
            //빈 페이저
            Pager(
                config = PagingConfig(PAGE_LIMIT)
            ) {
                object : PagingSource<Int, PostEntity>() {
                    override fun getRefreshKey(state: PagingState<Int, PostEntity>): Int? = null

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostEntity> {
                        return LoadResult.Page(
                            data = emptyList(),
                            prevKey = null,
                            nextKey = null
                        )
                    }
                }
            }.flow
        }
    }

    override fun getPostUserFilter(uid: String): Flow<PagingData<PostEntity>> {
        val query = firestore.collection(COLLECTION)
            .whereEqualTo("written.uid", uid)
            .orderBy("comment_user", Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = PostEntity::class.java
            )

        }.flow
    }

    override fun getPostItem(postId: String): Flow<List<PostEntity>> {
        val query = firestore.collection(COLLECTION)
            .whereEqualTo("id", postId)
            .limit(1)

        return query.dataObjects<PostEntity>()
    }

    override suspend fun addPost(postEntity: PostEntity) {
        firestore.collection(COLLECTION).document().let { document ->
            document.set(postEntity.copy(id = document.id))
                .addOnFailureListener { throw it }
                .addOnCanceledListener { throw CancellationException() }
                .await()
        }
    }

    override suspend fun editPost(postEntity: PostEntity): Result<Unit> {
        return try {
            firestore.collection(COLLECTION)
                .document(postEntity.id ?: return Result.Error(NullPointerException()))
                .set(postEntity)
                .await()
            Result.Success(Unit)
        } catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun updateOrInsertPost(postEntity: PostEntity) {
        firestore.collection(COLLECTION)
            .document(postEntity.id!!)
            .set(postEntity)
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    override suspend fun deletePost(postId: String) {
        firestore.collection(COLLECTION)
            .document(postId)
            .delete()
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    override suspend fun getPopularPostItems(): List<PostEntity> {
        return try {
            // 현재 시간에서 7일 전 시간 계산
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val sevenDaysAgo = Timestamp(calendar.time)

            val query = firestore.collection(COLLECTION)
                .whereGreaterThan("date_time.created", sevenDaysAgo)
                .orderBy("views", Query.Direction.DESCENDING)
                .limit(2)
                .get()
                .await()

            query.documents.mapNotNull { document ->
                document.toObject<PostEntity>()
            }
        } catch (e: Exception) {
            // 예외 처리
            emptyList()
        }
    }

    override suspend fun getTopNotice(): PostEntity {
        return try {
            val query = firestore.collection(COLLECTION)
                .whereEqualTo("category", "notice")
                .limit(1)
                .get()
                .await()

            val document = query.documents.firstOrNull()
            document?.toObject<PostEntity>() ?: PostEntity()
        } catch (e: Exception) {
            PostEntity()
        }
    }

    override suspend fun getPopularPostList(): List<PostEntity> {
        return try {
            // 현재 시간에서 7일 전 시간 계산
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val sevenDaysAgo = Timestamp(calendar.time)

            val query = firestore.collection(COLLECTION)
                .whereGreaterThan("date_time.created", sevenDaysAgo)
                .orderBy("views", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            // Firestore 쿼리 결과를 PostEntity 리스트로 변환
            query.documents.mapNotNull { document ->
                document.toObject<PostEntity>()
            }
        } catch (e: Exception) {
            // 예외가 발생하면 빈 리스트 반환
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPostForPostId(postId: String): PostEntity {
        return try {
            val query = firestore.collection(COLLECTION)
                .whereEqualTo("id", postId)
                .get()
                .await()

            val document = query.documents.firstOrNull()
            document?.toObject<PostEntity>() ?: PostEntity()
        } catch (e: Exception) {
            e.printStackTrace()
            PostEntity()
        }
    }

    override fun getCommentCount(postId: String): Flow<Long> = callbackFlow {
        firestore.collection(COMMENT_COLLECTION)
            .whereEqualTo("post_data.post_id", postId)
            .count()
            .get(AggregateSource.SERVER)
            .addOnSuccessListener {
                trySend(it.count)
            }
            .addOnFailureListener {
                trySend(-1)
            }
            .await()

        awaitClose()
    }

    override fun getLikeCount(postId: String): Flow<Long> = callbackFlow {
        firestore.collection(COLLECTION)
            .whereEqualTo("post_data.post_id", postId)
            .count()
            .get(AggregateSource.SERVER)
            .addOnSuccessListener {
                trySend(it.count)
            }
            .addOnFailureListener {
                trySend(-1)
            }
            .await()

        awaitClose()
    }

    override suspend fun addLike(postId: String, uid: String): Result<Unit> {
        val task = firestore.collection(COLLECTION)
            .document(postId)
            .update("like_user", FieldValue.arrayUnion(uid))
            .also { it.await() }

        if (task.isSuccessful) {
            return Result.Success(Unit)
        }
        return Result.Error(task.exception)
    }

    override suspend fun deleteLike(postId: String, uid: String): Result<Unit> {
        val task = firestore.collection(COLLECTION)
            .document(postId)
            .update("like_user", FieldValue.arrayRemove(uid))
            .also { it.await() }

        if (task.isSuccessful) {
            return Result.Success(Unit)
        }
        return Result.Error(task.exception)
    }

    override suspend fun addViews(postId: String): Result<Unit> {
        val task = firestore.collection(COLLECTION)
            .document(postId)
            .collection("_counter_shards_")
            .document()
            .set(mapOf("views" to 1))
            .also { it.await() }

        if (task.isSuccessful) {
            return Result.Success(Unit)
        }
        return Result.Error(task.exception)
    }

    override suspend fun setProfileImage(userId: String, imgUrl: String) {
        try {
            val getQuery = firestore.collection(COLLECTION)
                .whereEqualTo("written.uid", userId)
                .get()
                .await()

            val postList = getQuery.documents.mapNotNull { document ->
                document.toObject<PostEntity>()?.apply {
                    written?.profileImage = imgUrl
                }
            }

            postList.forEach { post ->
                firestore.collection(COLLECTION)
                    .document(post.id!!)
                    .set(post)
                    .await()
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun setUserDataForName(userEntity: UserEntity, name: String) {
        try {
            val getQuery = firestore.collection(COLLECTION)
                .whereEqualTo("written.uid", userEntity.uid!!)
                .get()
                .await()

            val postList = getQuery.documents.mapNotNull { document ->
                document.toObject<PostEntity>()?.apply {
                    written?.name = name
                }
            }

            postList.forEach { post ->
                firestore.collection(COLLECTION)
                    .document(post.id!!)
                    .set(post)
                    .await()
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun addBookmark(postId: String, uid: String): Result<Unit> {
        val task = firestore.collection(COLLECTION)
            .document(postId)
            .update("bookmark_user", FieldValue.arrayUnion(uid))
            .also { it.await() }

        if (task.isSuccessful) {
            return Result.Success(Unit)
        }
        return Result.Error(task.exception)
    }

    override suspend fun deleteBookmark(postId: String, uid: String): Result<Unit> {
        val task = firestore.collection(COLLECTION)
            .document(postId)
            .update("bookmark_user", FieldValue.arrayRemove(uid))
            .also { it.await() }

        if (task.isSuccessful) {
            return Result.Success(Unit)
        }
        return Result.Error(task.exception)
    }

    override fun getBookmarkItems(uid: String): Flow<PagingData<PostEntity>> {
        val query = firestore.collection(COLLECTION)
            .whereArrayContains("bookmark_user", uid)
            .limit(30)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = PostEntity::class.java
            )

        }
            .flow
    }

    companion object {
        private const val COLLECTION = "post"
        private const val COMMENT_COLLECTION = "comment"
        private const val PAGE_LIMIT = 30
    }


    private fun Query.whereNotNullEqualTo(field: String, value: Any?): Query {
        if (value == null) {
            return this
        }
        return whereEqualTo(field, value)
    }
}