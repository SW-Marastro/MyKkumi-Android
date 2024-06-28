package com.swmarastro.mykkumi.data.repository

import android.util.Log
import com.swmarastro.mykkumi.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor() : ImageRepository {
    override suspend fun loadImage(imageUrl: String): ByteArray? {
        return try {
            val url = URL(imageUrl)
            val stream = url.openStream()
            stream.readBytes()
        } catch (e: MalformedURLException) {
            //Log.d("LoadImage Error", "MalformedURLException occurred: ${e.message}")
            e.printStackTrace()
            null
        } catch (e: IOException) {
            //Log.d("LoadImage Error", "IOException occurred: ${e.message}")
            e.printStackTrace()
            null
        } catch (e: Exception) {
            //Log.d("LoadImage Error", "Unexpected Exception occurred: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}