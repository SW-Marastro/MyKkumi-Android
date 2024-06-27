package com.swmarastro.mykkumi.feature.home

import androidx.fragment.app.viewModels
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBannerAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBannerAllFragment : BaseFragment<FragmentHomeBannerAllBinding>(R.layout.fragment_home_banner_all) {

    private val viewModel by viewModels<HomeViewModel>({ requireActivity() })

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
    }

}