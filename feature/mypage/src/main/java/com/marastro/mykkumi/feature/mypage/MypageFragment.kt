package com.marastro.mykkumi.feature.mypage

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.mypage_screen))

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

//        // 로그아웃 테스트
//        binding.btnLogout.setOnClickListener {
//            viewModel.logout()
//            Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
//            onResume()
//        }
//
//        // 회원탈퇴 - 구글폼 연결
//        binding.btnDeleteUser.setOnClickListener {
//            val url = "https://forms.gle/A4dkrPLf7W3wwKYs6"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            startActivity(intent)
//        }
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