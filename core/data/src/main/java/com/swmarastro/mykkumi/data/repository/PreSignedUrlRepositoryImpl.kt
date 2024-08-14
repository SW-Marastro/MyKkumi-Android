package com.swmarastro.mykkumi.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.swmarastro.mykkumi.data.datasource.PreSignedUrlDataSource
import com.swmarastro.mykkumi.data.datasource.PutImageS3DataSource
import com.swmarastro.mykkumi.data.util.FormDataUtil
import com.swmarastro.mykkumi.domain.repository.PreSignedUrlRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreSignedUrlRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preSignedUrlDataSource: PreSignedUrlDataSource,
    private val putImageS3DataSource: PutImageS3DataSource,
) : PreSignedUrlRepository {
    override suspend fun getPreSignedPostUrl(imageLocalUri: Any): String? {
        try {
            val preSignedUrlResponse = preSignedUrlDataSource.getPreSignedPostUrl()

            val imageFile = FormDataUtil.convertUriToRequestBody(context, imageLocalUri)

            if (imageFile == null) {
                Toast.makeText(context, "손상된 이미지입니다.", Toast.LENGTH_SHORT).show()
                return null
            } else {
                try {
                    putImageS3DataSource.putImageForS3(
                        preSignedUrlResponse.url,
                        imageFile
                    )

                    val imageUrl: String = preSignedUrlResponse.url.split("?")[0]
                    return imageUrl
                } catch (e: Exception) {
                    return null
                }
            }
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun getPreSignedProfileUrl(imageLocalUri: Any): String? {
        try {
            val preSignedUrlResponse = preSignedUrlDataSource.getPreSignedProfileUrl()

            val imageFile = FormDataUtil.convertUriToRequestBody(context, imageLocalUri)

            if (imageFile == null) {
                Toast.makeText(context, "손상된 이미지입니다.", Toast.LENGTH_SHORT).show()
                return null
            } else {
                try {
                    putImageS3DataSource.putImageForS3(
                        preSignedUrlResponse.url,
                        imageFile
                    )

                    val imageUrl: String = preSignedUrlResponse.url.split("?")[0]
                    return imageUrl
                } catch (e: Exception) {
                    return null
                }
            }
        } catch (e: Exception) {
            return null
        }
    }
}