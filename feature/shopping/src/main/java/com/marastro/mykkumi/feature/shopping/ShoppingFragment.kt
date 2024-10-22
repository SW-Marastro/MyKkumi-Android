package com.marastro.mykkumi.feature.shopping

import androidx.fragment.app.viewModels
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.feature.shopping.databinding.FragmentShoppingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragment : BaseFragment<FragmentShoppingBinding>(
    R.layout.fragment_shopping
) {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel by viewModels<ShoppingViewModel>()

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.shopping_screen))

        binding.textTest.text = "${String(Character.toChars(0x1F525))} 열심히 준비 중입니다 ${String(Character.toChars(0x1F525))}"
    }
}