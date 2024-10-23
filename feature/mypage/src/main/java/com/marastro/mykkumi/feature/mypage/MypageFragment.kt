package com.marastro.mykkumi.feature.mypage

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
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
            binding.btnLogin.visibility = View.GONE          // 로그인 버튼

            binding.btnLogout.visibility = View.VISIBLE      // 로그아웃 버튼
            binding.btnDeleteUser.visibility = View.VISIBLE  // 회원탈퇴 버튼
        }
        // 로그인 되어있지 않을 때
        else {
            binding.btnLogin.visibility = View.VISIBLE       // 로그인 버튼

            binding.btnLogout.visibility = View.GONE         // 로그아웃 버튼
            binding.btnDeleteUser.visibility = View.GONE     // 회원탈퇴 버튼
        }
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.mypage_screen))

        binding.textTest.text = "${String(Character.toChars(0x1F525))} 열심히 준비 중입니다 ${String(Character.toChars(0x1F525))}"

        // 로그인 되어있을 때
        if(viewModel.isLogin()) {
            binding.btnLogin.visibility = View.GONE          // 로그인 버튼

            binding.btnLogout.visibility = View.VISIBLE      // 로그아웃 버튼
            binding.btnDeleteUser.visibility = View.VISIBLE  // 회원탈퇴 버튼
        }
        // 로그인 되어있지 않을 때
        else {
            binding.btnLogin.visibility = View.VISIBLE       // 로그인 버튼

            binding.btnLogout.visibility = View.GONE         // 로그아웃 버튼
            binding.btnDeleteUser.visibility = View.GONE     // 회원탈퇴 버튼
        }

        // 로그인
        binding.btnLogin.setOnClickListener {
            val intent = viewModel.navigateLogin()
            if(intent != null) {
                startActivity(intent)
            }
        }

        // 로그아웃 테스트
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            onResume()
        }

        // 회원탈퇴 - 구글폼 연결
        binding.btnDeleteUser.setOnClickListener {
            val url = "https://forms.gle/A4dkrPLf7W3wwKYs6"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}