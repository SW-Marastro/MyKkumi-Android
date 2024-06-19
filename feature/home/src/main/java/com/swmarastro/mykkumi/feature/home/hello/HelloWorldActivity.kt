package com.swmarastro.mykkumi.feature.home.hello

import android.os.Bundle
import androidx.activity.viewModels
import android.util.Log
import androidx.lifecycle.Observer
import com.swmarastro.mykkumi.common_ui.base.BaseActivity
import com.swmarastro.mykkumi.feature.home.R
import com.swmarastro.mykkumi.feature.home.databinding.ActivityHelloWorldBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

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
        Log.d("---view", "test1")
        viewModel.helloWorldUiState.collect { response ->
            binding.textHelloworld.text = response
        }
    }
}