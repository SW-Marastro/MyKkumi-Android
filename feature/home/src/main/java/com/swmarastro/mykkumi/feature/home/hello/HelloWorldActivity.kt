package com.swmarastro.mykkumi.feature.home.hello

import androidx.activity.viewModels
import android.util.Log
import com.swmarastro.mykkumi.common_ui.base.BaseActivity
import com.swmarastro.mykkumi.feature.home.R
import com.swmarastro.mykkumi.feature.home.databinding.ActivityHelloWorldBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HelloWorldActivity : BaseActivity<ActivityHelloWorldBinding> (
    R.layout.activity_hello_world
) {
    private val viewModel by viewModels<HelloWorldViewModel>()

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        //setTitle()
    }

    suspend fun setTitle() {
        viewModel.setHelloWorld()
        Log.d("---view", "test1")
        viewModel.helloWorldUiState.onEach {
            Log.d("---view", "test")
        }
    }
}