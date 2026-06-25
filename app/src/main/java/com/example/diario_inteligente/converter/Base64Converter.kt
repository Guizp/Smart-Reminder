package com.example.diario_inteligente.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream

object Base64Converter {

    fun drawableToString(drawable: Drawable): String {

        val bitmap = (drawable as BitmapDrawable).bitmap

        val outputStream = ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            70,
            outputStream
        )

        val byteArray = outputStream.toByteArray()

        return   Base64.encodeToString(
            byteArray,
            Base64.DEFAULT
        )
    }

    fun bitmapToString(bitmap: Bitmap): String {

        val outputStream = ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            70,
            outputStream
        )

        val byteArray = outputStream.toByteArray()

        return Base64.encodeToString(
            byteArray,
            Base64.DEFAULT
        )
    }

    fun stringToBitmap(imageString: String): Bitmap {

        val decodedBytes = Base64.decode(
            imageString,
            Base64.DEFAULT
        )

        return BitmapFactory.decodeByteArray(
            decodedBytes,
            0,
            decodedBytes.size
        )
    }
}