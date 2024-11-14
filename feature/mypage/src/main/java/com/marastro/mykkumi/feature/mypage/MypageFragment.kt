package com.marastro.mykkumi.feature.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.feature.mypage.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel by viewModels<MypageViewModel>()

    private var navController: NavController? = null

    override fun onResume() {
        super.onResume()

        // 로그인 되어있을 때
        if(viewModel.isLogin()) {
            binding.relativeLoginFalse.visibility = View.GONE
            binding.relativeLoginTrue.visibility = View.VISIBLE

            viewModel.getLoginUser(
                showToast = {
                    showToast(it)
                }
            )
        }
        // 로그인 되어있지 않을 때
        else {
            binding.relativeLoginFalse.visibility = View.VISIBLE
            binding.relativeLoginTrue.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.mypage_screen))

        navController = view.findNavController()

        observeUserInfo()

        // 로그인 되어있을 때
        if(viewModel.isLogin()) {
            binding.relativeLoginFalse.visibility = View.GONE
            binding.relativeLoginTrue.visibility = View.VISIBLE

            viewModel.getLoginUser(
                showToast = {
                    showToast(it)
                }
            )
        }
        // 로그인 되어있지 않을 때
        else {
            binding.relativeLoginFalse.visibility = View.VISIBLE
            binding.relativeLoginTrue.visibility = View.GONE
        }

        // 로그인
        binding.textBtnLogin.setOnClickListener(View.OnClickListener {
            val intent = viewModel.navigateLogin()
            if(intent != null) {
                startActivity(intent)
            }
        })

        // 설정 페이지
        binding.btnSetting.setOnClickListener {
            val settingDeepLink = "mykkumi://setting"
            navController?.navigate(deepLink = settingDeepLink.toUri())
        }

        // 프로필 수정 페이지
//        binding.textEditProfile.setOnClickListener(View.OnClickListener {
//            val settingDeepLink = "mykkumi://profile.edit"
//            navController?.navigate(deepLink = settingDeepLink.toUri())
//        })
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
    }

    private fun observeUserInfo() {
        // 사용자 정보 observe
        viewModel.userInfoUiState.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                binding.textNickname.text = it.nickname
                binding.textIntroduce.text = it.introduction
                Glide
                    .with(requireContext())
                    .load(it.profileImage)
                    .placeholder(com.marastro.mykkumi.common_ui.R.drawable.img_profile_default)
                    .circleCrop()
                    .into(binding.imgProfile)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}