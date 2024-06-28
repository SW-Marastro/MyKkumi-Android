package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBannerAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBannerAllFragment : BaseFragment<FragmentHomeBannerAllBinding>(R.layout.fragment_home_banner_all) {

    private val viewModel by viewModels<HomeViewModel>({ requireActivity() })
    private lateinit var bannerAllAdapter: HomeBannerAllAdapter

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

        setBannerAll() // 배너 리스트
    }

    // 배너 viewpager
    private fun initBannerRecyclerView(banners: HomeBannerListVO) {
        bannerAllAdapter = HomeBannerAllAdapter(
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
        viewModel.homeBannerUiState.collect { response ->
            initBannerRecyclerView(response)
        }
    }

    // 배너 클릭 -> 배너 상세 페이지 이동
    private fun onClickBannerItem(bannerId: Int) {
        viewModel.selectHomeBanner(bannerId)
        view?.findNavController()?.navigate(R.id.action_navigate_fragment_to_home_banner_detail)
    }

    private fun onClickBack() {
        binding.btnCloseBannerAll.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }
}