package com.marastro.mykkumi.feature.home.banner

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.feature.home.databinding.FragmentHomeBannerDetailBinding
import com.bumptech.glide.Glide
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.feature.home.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeBannerDetailFragment : BaseFragment<FragmentHomeBannerDetailBinding>(R.layout.fragment_home_banner_detail) {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel by viewModels<HomeBannerDetailViewModel>()
    private val args: HomeBannerDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.banner_detail_screen))

        binding.btnCloseBannerDetail.setOnClickListener {
            view.findNavController()?.popBackStack()
        }
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        setBannerDetail()
    }

    private suspend fun setBannerDetail() {
        viewModel.setBannerDetail(args.bannerId)
        viewModel.bannerDetailUiState.collect { response ->
            Glide
                .with(this)
                .load(response.imageUrl)
                .into(binding.imageBannerDetail)
        }
    }
}