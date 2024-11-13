package com.marastro.mykkumi.feature.mypage.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

            // 개인정보처리방침
            binding.relativePersonalInfoNotice.setOnClickListener(View.OnClickListener {
                val url = "http://swmarastro.notion.site"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            })

            // 로그아웃
            binding.relativeLogout.setOnClickListener(View.OnClickListener {
                val dialog = LogoutConfirmDialog(this, analyticsHelper)
                dialog.setOnClickListener {
                    viewModel.logout()
                    Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()

                    // 마이페이지로
                    navController?.popBackStack()
                }
                dialog.show()
            })

            // 회원탈퇴
            binding.relativeDeleteUser.setOnClickListener(View.OnClickListener {

            })
        }

        override suspend fun initView() {
            bind {
                vm = viewModel
            }
        }
    }