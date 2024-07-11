package com.swmarastro.mykkumi.android

import android.app.Application
import com.swmarastro.mykkumi.data.util.KakaoInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/*
@HiltAndroidApp: 적용된 Spplication 클래스를 포함한 Hilt의 코드 생성을 트리거
Hilt를 사용하는 모든 앱은 Application 클래스에 @HiltAndoidApp 이라는 어노테이션을 적용해야 함.
*/
@HiltAndroidApp
class MyKkumiApplication : Application() {
    @Inject
    lateinit var kakaoInitializer: KakaoInitializer

    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        kakaoInitializer.initialize()
    }
}