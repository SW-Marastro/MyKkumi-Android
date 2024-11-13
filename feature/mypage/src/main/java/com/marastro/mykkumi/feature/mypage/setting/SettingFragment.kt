package com.marastro.mykkumi.feature.mypage.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.feature.mypage.R
import com.marastro.mykkumi.feature.mypage.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting
) {
        @Inject
        lateinit var analyticsHelper: AnalyticsHelper

        private val viewModel by viewModels<SettingViewModel>()

        private var navController: NavController? = null

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Firebase Analytics 화면 이름 로깅
            analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.setting_screen))

            navController = view.findNavController()

            // 이전 버튼
            binding.btnBack.setOnClickListener {
                navController?.popBackStack()
            }

            // 앱 버전
            binding.textAppVersionContent.text = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName

        }

        override suspend fun initView() {
            bind {
                vm = viewModel
            }
        }
    }