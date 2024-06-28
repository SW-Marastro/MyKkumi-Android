package com.swmarastro.mykkumi.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

object ImageLoader {
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}