package com.swmarastro.mykkumi.feature.home.hello

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swmarastro.mykkumi.common_ui.base.BaseActivity
import com.swmarastro.mykkumi.feature.home.R
import com.swmarastro.mykkumi.feature.home.databinding.ActivityHelloWorldBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelloWorldActivity : BaseActivity<ActivityHelloWorldBinding>(R.layout.activity_hello_world) {
    private val viewModel by viewModels<HelloWorldViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
}