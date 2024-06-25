package com.swmarastro.mykkumi.feature.shopping

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.shopping.databinding.FragmentShoppingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : BaseFragment<FragmentShoppingBinding>(
    R.layout.fragment_shopping
) {

    private val viewModel by viewModels<ShoppingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        lifecycleScope.launchWhenCreated {
            initView()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        setTitle()
    }

    suspend fun setTitle() {
        viewModel.setHelloWorld()
        viewModel.shoppingUiState.collect { response ->
            binding.textHelloworld.text = response
        }
    }
}