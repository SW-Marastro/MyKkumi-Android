package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        lifecycleScope.launchWhenCreated {
            initView()
        }
        super.onViewCreated(view, savedInstanceState)

        // 배너 테스트용
        /*val bannerList: HomeBannerListVO = HomeBannerListVO(
            mutableListOf(
                HomeBannerVO(1, "https://avatars.githubusercontent.com/u/76805879?v=4"),
                HomeBannerVO(2, "https://avatars.githubusercontent.com/u/168630394?s=200&v=4")
            )
        )
        initBannerViewPager(bannerList)*/
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        setHomeBanner() // 배너
    }

    private fun initBannerViewPager(banners: HomeBannerListVO) {
        binding.viewpagerBanner.adapter = HomeBannerViewPagerAdapter(mutableListOf())
        binding.viewpagerBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.lifecycleOwner = this
        lifecycleScope.launchWhenStarted {
            viewModel.bannerImageUiState.collect { bitmaps ->
                binding.viewpagerBanner.adapter?.let {
                    if (it is HomeBannerViewPagerAdapter) {
                        it.setImages(bitmaps)
                    }
                }
            }
        }

        viewModel.loadImages(banners.banners)
    }

    suspend fun setHomeBanner() {
        viewModel.setHomeBanner()
        viewModel.homeBannerUiState.collect { response ->
            initBannerViewPager(response)
        }
    }
}