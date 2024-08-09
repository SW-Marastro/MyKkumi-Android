package com.swmarastro.mykkumi.data.repository

import android.util.Log
import com.swmarastro.mykkumi.data.datasource.PreSignedUrlDataSource
import com.swmarastro.mykkumi.domain.repository.PreSignedUrlRepository
import javax.inject.Inject

class PreSignedUrlRepositoryImpl @Inject constructor(
    private val preSignedUrlDataSource: PreSignedUrlDataSource
) : PreSignedUrlRepository {
    override suspend fun getPreSignedUrl(): String {
        val preSignedUrlResponse = preSignedUrlDataSource.getPreSignedUrl()
        //Log.d("test", preSignedUrlResponse.url)
        return ""
    }
}