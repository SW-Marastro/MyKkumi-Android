package com.swmarastro.mykkumi.feature.home.hello

import android.os.Bundle
import androidx.activity.viewModels
import com.swmarastro.mykkumi.common_ui.base.BaseActivity
import com.swmarastro.mykkumi.feature.home.R
import com.swmarastro.mykkumi.feature.home.databinding.ActivityHelloWorldBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope

@AndroidEntryPoint
class HelloWorldActivity : BaseActivity<ActivityHelloWorldBinding> (
    R.layout.activity_hello_world
) {
    private val viewModel by viewModels<HelloWorldViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        lifecycleScope.launchWhenCreated {
            initView()
        }
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        setTitle()
    }

    suspend fun setTitle() {
        viewModel.setHelloWorld()
        viewModel.helloWorldUiState.collect { response ->
            binding.textHelloworld.text = response
        }
    }
}