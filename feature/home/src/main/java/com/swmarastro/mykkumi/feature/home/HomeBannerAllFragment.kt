package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBannerAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBannerAllFragment : BaseFragment<FragmentHomeBannerAllBinding>(R.layout.fragment_home_banner_all) {

    private val viewModel by viewModels<HomeViewModel>({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        lifecycleScope.launchWhenCreated {
            initView()
        }
        super.onViewCreated(view, savedInstanceState)

        onClickBack() // 창
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
    }

    private fun onClickBack() {
        binding.btnCloseBannerAll.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }
}