package com.swmarastro.mykkumi.data.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FormDataUtil {
    // Uri를 Multipart/form-data로 변환
    fun convertUriToMultipart(context: Context, image: Any?): MultipartBody.Part? {
        val imageUri = anyToUri(image)

        Log.d("test url", imageUri.toString())

        if(imageUri == null) return null

        val file = uriToFile(context, imageUri)

        if(file == null || !file.exists()) return null

        val requestFile = file.asRequestBody("image/*".toMediaType())
        return MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
    }

    // Content URI를 파일로 변환하는 함수
    fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val fileName = getFileName(contentResolver, uri)
        val tempFile = File(context.cacheDir, fileName)

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

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

    // 파일 이름을 얻는 함수
    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var name = "temp_file"
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

    fun getBody(value: String?): RequestBody? {
        return value?.toRequestBody("text/plain".toMediaType())
    }

    fun getListLongBody(value: List<Long>?): RequestBody? {
        val json = Gson().toJson(value)
        return json.toRequestBody("application/json; charset=utf-8".toMediaType())
    }

    fun anyToUri(imageUri: Any?): Uri? {
        return if(imageUri is Uri) {
            imageUri
        } else {
            null
        }
    }
}


