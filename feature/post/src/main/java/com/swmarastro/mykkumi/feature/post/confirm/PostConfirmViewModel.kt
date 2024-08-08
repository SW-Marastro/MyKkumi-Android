package com.swmarastro.mykkumi.feature.post.confirm

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostConfirmViewModel @Inject constructor(
) : ViewModel() {
    // 사용자의 응답
    fun submitUserCallback(navController: NavController?, callback: Boolean) {
        navController?.previousBackStackEntry?.savedStateHandle?.set("callback", callback)
        navController?.popBackStack()
    }
}