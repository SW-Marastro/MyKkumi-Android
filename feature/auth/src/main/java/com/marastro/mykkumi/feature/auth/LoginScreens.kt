package com.marastro.mykkumi.feature.auth

enum class LoginScreens {
    KakaoLoginScreen,
    LoginSelectHobbyScreen,
    LoginInputUserScreen;

    companion object {
        fun fromRoute(route: String?) : LoginScreens
            = when (route?.substringBefore("/")) {
                LoginSelectHobbyScreen.name -> LoginSelectHobbyScreen
                LoginInputUserScreen.name -> LoginInputUserScreen
                null -> KakaoLoginScreen
                else -> throw  IllegalArgumentException("Route $route is not proper")
            }
    }
}