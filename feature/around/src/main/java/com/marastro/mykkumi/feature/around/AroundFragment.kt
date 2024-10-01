package com.marastro.mykkumi.feature.around

import androidx.fragment.app.viewModels
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.feature.around.databinding.FragmentAroundBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AroundFragment : BaseFragment<FragmentAroundBinding>(R.layout.fragment_around) {

    private val viewModel by viewModels<AroundViewModel>()

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        binding.textTest.text = "${String(Character.toChars(0x1F525))} 열심히 준비 중입니다 ${String(Character.toChars(0x1F525))}"
    }

}