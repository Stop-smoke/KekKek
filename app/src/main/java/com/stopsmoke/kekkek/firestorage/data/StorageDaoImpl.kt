package com.stopsmoke.kekkek.firestorage.data

import android.net.Uri
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.stopsmoke.kekkek.firestorage.dao.StorageDao
import com.stopsmoke.kekkek.firestorage.model.StorageDeleteResult
import com.stopsmoke.kekkek.firestorage.model.StorageUploadResult
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

    override fun uploadFile(
        inputStream: InputStream,
        path: String
    ): Flow<StorageUploadResult> = callbackFlow {
        val downloadUrl = OnSuccessListener<Uri> {
            trySendBlocking(StorageUploadResult.Success(it.toString()))
        }

        val storageReference = reference.child(path)

        val progressListener = OnProgressListener<UploadTask.TaskSnapshot> {
            trySendBlocking(StorageUploadResult.Progress)
        }
        val successListener = OnSuccessListener<UploadTask.TaskSnapshot> {
            storageReference.downloadUrl.addOnSuccessListener(downloadUrl)
        }
        val failureListener = OnFailureListener {
            trySendBlocking(StorageUploadResult.Error(it))
        }

        val uploadTask = storageReference.putStream(inputStream).apply {
            addOnProgressListener(progressListener)
            addOnSuccessListener(successListener)
            addOnFailureListener(failureListener)
        }
        uploadTask.await()

        awaitClose {
            uploadTask.removeOnFailureListener(failureListener)
            uploadTask.removeOnSuccessListener(successListener)
            uploadTask.removeOnProgressListener(progressListener)
        }
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
            await()
        }

        awaitClose()
    }

}