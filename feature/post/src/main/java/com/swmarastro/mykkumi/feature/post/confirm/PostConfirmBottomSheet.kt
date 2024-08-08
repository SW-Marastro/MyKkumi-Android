package com.swmarastro.mykkumi.feature.post.confirm

import android.os.Bundle
import android.view.View
import com.swmarastro.mykkumi.common_ui.base.BaseBottomSheetFragment
import com.swmarastro.mykkumi.feature.post.R
import com.swmarastro.mykkumi.feature.post.databinding.FragmentConfirmBottomSheetBinding

class PostConfirmBottomSheet : BaseBottomSheetFragment<FragmentConfirmBottomSheetBinding>(R.layout.fragment_confirm_bottom_sheet){

    private var postConfirmListener: PostConfirmListener? = null
    private var position: Int = -1

    interface PostConfirmListener {
        fun confirmAgree(position: Int) // 동의
        fun confirmCancel()
    }

    fun setListener(postConfirmListener: PostConfirmListener) {
        this.postConfirmListener = postConfirmListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 취소 버튼
        binding.btnConfirmCancel.setOnClickListener {
            postConfirmListener?.confirmCancel()
            dismiss()
        }

        // 확인 버튼
        binding.btnConfirmAgree.setOnClickListener {
            postConfirmListener?.confirmAgree(position)
            dismiss()
        }
    }

    override suspend fun initView() {
        // 안내 문구
        binding.textConfirmNoticeMessage.text = arguments?.getString("message")

        // 선택된 position
        position = arguments?.getInt("position") ?: -1
    }
}