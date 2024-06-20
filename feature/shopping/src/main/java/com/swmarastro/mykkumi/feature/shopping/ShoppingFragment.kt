package com.swmarastro.mykkumi.feature.shopping

import androidx.fragment.app.viewModels
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.shopping.databinding.FragmentShoppingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : BaseFragment<FragmentShoppingBinding>(R.layout.fragment_shopping) {

    private val viewModel by viewModels<ShoppingViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
}