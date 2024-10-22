package com.marastro.mykkumi.feature.post.confirm

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.feature.post.databinding.DialogPostDeleteImageConfirmBinding

class PostDeleteImageConfirmDialog(
    private val context: Fragment,
    private val analyticsHelper: AnalyticsHelper
) {

    private val binding by lazy { DialogPostDeleteImageConfirmBinding.inflate(context.layoutInflater) }
    private val dialog = Dialog(context.requireContext())

    // 리스너 람다 초기화
    private var confirmListener: (Int) -> Unit = {}
    private var cancelListener: () -> Unit = {}

    // 포스트 리포트 확인 리스너 설정 함수
    fun confirmPostReportListener(listener: (Int) -> Unit) {
        this.confirmListener = listener
    }

    // 포스트 리포트 취소 리스너 설정 함수
    fun cancelPostReportListener(listener: () -> Unit) {
        this.cancelListener = listener
    }

    // 다이얼로그 보여주기 함수
    fun show(postId: Int) {
        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(context.requireContext(), com.marastro.mykkumi.analytics.R.string.delete_image_dialog))

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게
        dialog.setContentView(binding.root)

        // 확인 버튼 클릭 시
        binding.textBtnPostDeleteImageConfirm.setOnClickListener {
            confirmListener(postId) // 설정된 리스너 호출
            dialog.dismiss()
        }

        // 취소 버튼 클릭 시
        binding.textBtnPostDeleteImageCancel.setOnClickListener {
            cancelListener() // 설정된 리스너 호출
            dialog.dismiss()
        }

        dialog.show()
    }
}