package com.stopsmoke.kekkek.core.firestorage.data

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import com.stopsmoke.kekkek.core.firestorage.dao.StorageDao
import com.stopsmoke.kekkek.core.firestorage.model.StorageDeleteResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import javax.inject.Inject

internal class StorageDaoImpl @Inject constructor(
    private val reference: StorageReference,
) : StorageDao {

    override suspend fun uploadFile(
        inputStream: InputStream,
        path: String
    ): String {
        val storageReference = reference.child(path)
        storageReference.putStream(inputStream).await()

        val downloadUrl = storageReference.downloadUrl.await()
        return downloadUrl.toString()
    }
    override fun deleteFile(path: String): Flow<StorageDeleteResult> = callbackFlow {
        val successListener = OnSuccessListener<Void> {
            trySendBlocking(StorageDeleteResult.Success)
        }
        val failureListener = OnFailureListener {
            trySendBlocking(StorageDeleteResult.Error(it))
        }

        reference.child(path).delete().apply {
            addOnSuccessListener(successListener)
            addOnFailureListener(failureListener)
        }

        awaitClose()
    }

}