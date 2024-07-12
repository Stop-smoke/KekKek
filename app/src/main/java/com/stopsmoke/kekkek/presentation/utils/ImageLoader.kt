package com.stopsmoke.kekkek.presentation.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL

object UrlToBitmapImageLoader {
    suspend fun loadImage(imageUrl: String): Bitmap?{
        try{
            val url = URL(imageUrl)
            val stream = url.openStream()

            return BitmapFactory.decodeStream(stream)
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }
}