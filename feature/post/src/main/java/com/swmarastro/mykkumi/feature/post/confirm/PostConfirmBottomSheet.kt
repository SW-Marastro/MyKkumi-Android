package com.swmarastro.mykkumi.feature.post.confirm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.swmarastro.mykkumi.common_ui.base.BaseBottomSheetFragment
import com.swmarastro.mykkumi.feature.post.R
import com.swmarastro.mykkumi.feature.post.databinding.FragmentConfirmBottomSheetBinding

class PostConfirmBottomSheet : BaseBottomSheetFragment<FragmentConfirmBottomSheetBinding>(R.layout.fragment_confirm_bottom_sheet){
    private val viewModel by viewModels<PostConfirmViewModel>()
    private val args: PostConfirmBottomSheetArgs by navArgs()

    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        // 안내 문구
        binding.textConfirmNoticeMessage.text = args.message

        // 취소 버튼
        binding.btnConfirmCancel.setOnClickListener {
            viewModel.submitUserCallback(navController, false)
        }

        // 확인 버튼
        binding.btnConfirmAgree.setOnClickListener {
            viewModel.submitUserCallback(navController, true)
        }
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
    }
}