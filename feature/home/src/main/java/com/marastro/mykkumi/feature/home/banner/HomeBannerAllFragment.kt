package com.marastro.mykkumi.feature.home.banner

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.domain.entity.BannerListVO
import com.marastro.mykkumi.feature.home.R
import com.marastro.mykkumi.feature.home.databinding.FragmentHomeBannerAllBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeBannerAllFragment : BaseFragment<FragmentHomeBannerAllBinding>(R.layout.fragment_home_banner_all) {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel by viewModels<HomeBannerAllViewModel>()
    private lateinit var bannerAllAdapter: HomeBannerAllAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        super.onViewCreated(view, savedInstanceState)

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.banner_all_screen))

        onClickBack() // 창
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        setBannerAll() // 배너 리스트
    }

    // 배너 리스트 recyclerview
    private fun initBannerRecyclerView(banners: BannerListVO) {
        bannerAllAdapter = HomeBannerAllAdapter(
            requireContext(),
            banners.banners.toMutableList(),
            onClickBannerItem = {
                onClickBannerItem(it)
            }
        )
        binding.recyclerviewBannerAll.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerviewBannerAll.adapter = bannerAllAdapter
    }

    // 배너 내용 세팅
    private suspend fun setBannerAll() {
        viewModel.setHomeBanner()
        viewModel.bannerListUiState.collect { response ->
            initBannerRecyclerView(response)
        }
    }

    // 배너 클릭 -> 배너 상세 페이지 이동
    private fun onClickBannerItem(bannerId: Int) {
        viewModel.selectHomeBanner(bannerId)
        viewModel.navigateBannerDetail(view?.findNavController())
    }

    private fun onClickBack() {
        binding.btnCloseBannerAll.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }
}