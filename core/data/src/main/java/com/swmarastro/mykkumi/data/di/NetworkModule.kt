package com.swmarastro.mykkumi.data.di

import dagger.Module
/*
@Module: 인터페이스나, 빌더 패턴을 사용한 경우, 외부 라이브러리 클래스 등등 생성자를 사용할 수 없는 Class를 주입해야 할 경우
@InstallIn: 어떤 Component에 Install할지 모듈 범위 지정. @Module이나 @EntryPoint 어노테이션과 함께 사용해야 함.
(SingletonComponent::class): SingletonComponent에 설치한다는 뜻
*/
@Module
object NetworkModule {
}