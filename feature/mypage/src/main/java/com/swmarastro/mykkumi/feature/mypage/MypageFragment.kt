package com.swmarastro.mykkumi.feature.mypage

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
            viewModel.logout()
            Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}