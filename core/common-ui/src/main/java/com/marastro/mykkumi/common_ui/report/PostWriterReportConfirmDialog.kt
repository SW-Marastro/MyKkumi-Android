package com.marastro.mykkumi.common_ui.report

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.databinding.DialogPostWriterReportConfirmBinding

class PostWriterReportConfirmDialog(
    private val context: Fragment,
    private val analyticsHelper: AnalyticsHelper
) {
    private val binding by lazy { DialogPostWriterReportConfirmBinding.inflate(context.layoutInflater) }

    private val dialog = Dialog(context.requireContext())
    private lateinit var listener: PostWriterReportConfirmListener

    interface PostWriterReportConfirmListener {
        fun confirmPostWriterReport(userUuid: String)
    }

    fun setOnClickListener(listener: (String) -> Unit) {
        this.listener = object: PostWriterReportConfirmListener {
            override fun confirmPostWriterReport(userUuid: String) {
                listener(userUuid)
            }
        }
    }

    fun show(userUuid: String) {
        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(context.requireContext(), com.marastro.mykkumi.analytics.R.string.report_user_dialog))

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게
        dialog.setContentView(binding.root)

        binding.textBtnPostWriterReportConfirm.setOnClickListener(View.OnClickListener {
            listener.confirmPostWriterReport(userUuid = userUuid)
            dialog.dismiss()
        })

        binding.textBtnPostWriterReportCancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }
}