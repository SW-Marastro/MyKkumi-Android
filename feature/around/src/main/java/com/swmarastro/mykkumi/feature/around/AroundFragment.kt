package com.swmarastro.mykkumi.feature.around

import androidx.fragment.app.viewModels
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.around.databinding.FragmentAroundBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AroundFragment : BaseFragment<FragmentAroundBinding>(R.layout.fragment_around) {

    private val viewModel by viewModels<AroundViewModel>()

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
    }

}