package com.swmarastro.mykkumi.feature.mypage

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
    }
}