package com.swmarastro.mykkumi.feature.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBannerDetailBinding
import coil.load
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBannerDetailFragment : BaseFragment<FragmentHomeBannerDetailBinding>(R.layout.fragment_home_banner_detail) {

    private val viewModel by viewModels<HomeViewModel>({ requireActivity() })

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        setBannerDetail()
    }

    private suspend fun setBannerDetail() {
        viewModel.selectBannerId.observe(viewLifecycleOwner) { bannerId ->
            viewModel.setBannerDetail(bannerId)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.homeBannerDetailUiState.collect { response ->
                binding.imageBannerDetail.load(response.imageUrl)
            }
        }
    }
}