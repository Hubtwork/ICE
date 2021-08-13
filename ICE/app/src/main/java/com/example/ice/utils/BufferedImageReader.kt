package com.example.ice.utils
import android.R.attr
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.lang.Exception

object BufferedImageReader {

    fun decodeImageToBase64(base64String: String): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(base64String, Base64.URL_SAFE)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            DebugLogger.log("ERROR", e.localizedMessage)
            null
        }
    }

    fun encodeImageToBase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 70, baos)
        val bytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun bitmapToByteArray(image: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        return baos.toByteArray()
    }
}