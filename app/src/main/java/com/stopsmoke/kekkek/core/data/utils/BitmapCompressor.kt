package com.stopsmoke.kekkek.core.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.stopsmoke.kekkek.common.exception.FileTooLargeException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class BitmapCompressor(
    private val imageInputStream: InputStream
) {

    private val temp = File.createTempFile("image_compress_file", ".jpeg")

    fun getCompressedFile(): File {
        FileOutputStream(temp).buffered().compressed()
        checkFileSize()
        return temp
    }

    fun deleteOnExit() {
        temp.deleteOnExit()
    }

    private fun OutputStream.compressed() = use {
        val fileSize = imageInputStream.available()
        val bitmap = BitmapFactory.decodeStream(imageInputStream)
        val quality = getProfileImageQuality(fileSize)

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, this)
        bitmap.recycle()
    }

    private fun getProfileImageQuality(fileSize: Int): Int = when  {
        fileSize < 1000000 -> 75
        fileSize < 2000000 -> 40
        fileSize < 4000000 -> 20
        else -> 1
    }

    private fun checkFileSize() {
        if (temp.length() > 1000000) {
            throw FileTooLargeException()
        }
    }
}