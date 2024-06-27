package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>({ requireActivity() })
    private lateinit var bannerAdapter: HomeBannerViewPagerAdapter
    private lateinit var timer: Timer

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

        startAutoScroll()
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        setHomeBanner() // 배너
    }

    // 배너 viewpager
    private fun initBannerViewPager(banners: HomeBannerListVO) {
        bannerAdapter = HomeBannerViewPagerAdapter(
            banners.banners.toMutableList(),
            onClickBannerItem = {
                onClickBannerItem(it)
            }
        )
        binding.viewpagerBanner.adapter = bannerAdapter
        binding.viewpagerBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewpagerBanner.setCurrentItem(1000, false) // 좌측으로도 배너 전환 가능하도록

        // 배너 페이지 표시
        binding.textBannerTotalPage.text = "/" + banners.banners.size
        binding.textBannerCurrentPage.text = "1"

        /*binding.lifecycleOwner = this
        lifecycleScope.launchWhenStarted {
            viewModel.bannerImageUiState.collect { bitmaps ->
                binding.viewpagerBanner.adapter?.let {
                    if (it is HomeBannerViewPagerAdapter) {
                        it.setImages(bitmaps)
                    }
                }

            }
        }
        viewModel.loadImages(banners.banners)*/

        // 배너가 수동으로 변경되면, 자동 전환되는 타이머를 리셋 - 변경된 시점부터 3초 카운트
        binding.viewpagerBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                timer.cancel()    // 기존 타이머 중지
                startAutoScroll() // 타이머 리셋

                // 현재 배너 페이지 표시
                if(banners.banners.size != 0)
                    binding.textBannerCurrentPage.text = (binding.viewpagerBanner.currentItem % banners.banners.size + 1).toString()
            }
        })
    }

    // 배너 내용 세팅
    private suspend fun setHomeBanner() {
        viewModel.setHomeBanner()
        viewModel.homeBannerUiState.collect { response ->
            initBannerViewPager(response)
        }
    }

    // 배너 자동 전환
    private fun startAutoScroll() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                binding.viewpagerBanner.post {
                    binding.viewpagerBanner.currentItem = (binding.viewpagerBanner.currentItem + 1) % Int.MAX_VALUE
                }
            }
        }, 3000, 3000) // 3초마다 전환 -> 너무 빠른가?
    }

    // 배너 클릭
    private fun onClickBannerItem(bannerId: Int) {
        viewModel.selectHomeBanner(bannerId)
        view?.findNavController()?.navigate(R.id.action_navigate_fragment_to_home_banner_detail)
    }

    override fun onDestroyView() {
        _binding = null
        timer.cancel()
        super.onDestroyView()
    }
}