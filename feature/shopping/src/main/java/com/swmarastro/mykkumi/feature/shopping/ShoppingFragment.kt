package com.swmarastro.mykkumi.feature.shopping

import androidx.fragment.app.viewModels
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.shopping.databinding.FragmentShoppingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : BaseFragment<FragmentShoppingBinding>(
    R.layout.fragment_shopping
) {

    private val viewModel by viewModels<ShoppingViewModel>()

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        binding.textTest.text = "${String(Character.toChars(0x1F525))} 열심히 준비 중입니다 ${String(Character.toChars(0x1F525))}"
    }
}