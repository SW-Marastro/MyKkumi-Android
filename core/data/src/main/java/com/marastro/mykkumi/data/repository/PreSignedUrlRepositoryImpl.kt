package com.marastro.mykkumi.data.repository

import android.content.Context
import android.widget.Toast
import com.marastro.mykkumi.data.datasource.PreSignedUrlDataSource
import com.marastro.mykkumi.data.datasource.PutImageS3DataSource
import com.marastro.mykkumi.data.util.FormDataUtil
import com.marastro.mykkumi.domain.repository.PreSignedUrlRepository
import dagger.hilt.android.qualifiers.ApplicationContext
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
                        preSignedUrlResponse.presignedUrl,
                        imageFile
                    )

                    val imageUrl: String = preSignedUrlResponse.cdnUrl
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
                        preSignedUrlResponse.presignedUrl,
                        imageFile
                    )

                    val imageUrl: String = preSignedUrlResponse.cdnUrl
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