package com.marastro.mykkumi.data.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FormDataUtil {
    private val MAX_IMAGE_SIZE = 1024

    // Uri를 Multipart/form-data로 변환
    fun convertUriToRequestBody(context: Context, imageUri: Any?): RequestBody? {
        val imageUri = imageUri as Uri

        val file = uriToFile(context, imageUri)

        if(file == null || !file.exists()) return null

        val requestFile = file.asRequestBody("image/*".toMediaType())
        return requestFile
    }

    // Content URI를 파일로 변환하는 함수
    fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val fileName = getFileName(contentResolver, uri)
        val tempFile = File(context.cacheDir, fileName)

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)

            val exif = ExifInterface(inputStream!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            // 이미지 로드
            var bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))

            // 이미지 회전
            bitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                else -> bitmap
            }

            // 최대 width, height를 1024px로
            bitmap = resizeImage(bitmap)
            inputStream.close()

            val outputStream = FileOutputStream(tempFile)
            // 압축 품질 0~100 - 낮을수록 더 많이 압축됨
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            Log.e("FormDataUtil", "Error converting URI to File: ${e.message}")
            return null
        }

        return tempFile
    }

    // 이미지 크기 조정 함수 (비율 유지)
    fun resizeImage(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val newWidth: Int
        val newHeight: Int

        if (width > height) {
            newWidth = MAX_IMAGE_SIZE
            newHeight = (MAX_IMAGE_SIZE * height.toFloat() / width.toFloat()).toInt()
        } else {
            newWidth = (MAX_IMAGE_SIZE * width.toFloat() / height.toFloat()).toInt()
            newHeight = MAX_IMAGE_SIZE
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    // 이미지 회전
    fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // 파일 이름을 얻는 함수
    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var name = "image"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    name = cursor.getString(index)
                }
            }
        }
        return name
    }
}


