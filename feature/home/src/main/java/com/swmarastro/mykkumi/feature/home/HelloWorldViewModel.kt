package com.swmarastro.mykkumi.feature.home

import androidx.lifecycle.ViewModel
import com.swmarastro.mykkumi.domain.usecase.GetHelloWorldUseCase
import javax.inject.Inject

class HelloWorldViewModel @Inject constructor(
    private val getHelloWorldUseCase: GetHelloWorldUseCase
) : ViewModel() {
    //private
}