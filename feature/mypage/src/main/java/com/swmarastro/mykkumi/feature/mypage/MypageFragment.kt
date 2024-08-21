package com.swmarastro.mykkumi.feature.mypage

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.mypage.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val viewModel by viewModels<MypageViewModel>()

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        binding.textTest.text = "${String(Character.toChars(0x1F525))} 열심히 준비 중입니다 ${String(Character.toChars(0x1F525))}"

        // 로그아웃 테스트
        binding.btnLogout.setOnClickListener {
            if(viewModel.isLogin()) {
                viewModel.logout()
                Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, "로그인 해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원탈퇴 - 구글폼 연결
        binding.btnDeleteUser.setOnClickListener {
            if(viewModel.isLogin()) {
                val url = "https://forms.gle/A4dkrPLf7W3wwKYs6"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            else {
                Toast.makeText(context, "로그인 해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}