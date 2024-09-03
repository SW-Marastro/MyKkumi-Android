package com.swmarastro.mykkumi.feature.home.banner

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBannerDetailBinding
import coil.load
import com.swmarastro.mykkumi.feature.home.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBannerDetailFragment : BaseFragment<FragmentHomeBannerDetailBinding>(R.layout.fragment_home_banner_detail) {

    private val viewModel by viewModels<HomeBannerDetailViewModel>()
    private val args: HomeBannerDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCloseBannerDetail.setOnClickListener {
            view?.findNavController()?.popBackStack()
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
            binding.imageBannerDetail.load(response.imageUrl)
        }
    }
}