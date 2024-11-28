package com.marastro.mykkumi.feature.home.post

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.feature.home.databinding.DialogPostDeleteConfirmBinding

class PostDeleteConfirmDialog(
    private val context: Fragment,
    private val analyticsHelper: AnalyticsHelper
) {
    private val binding by lazy { DialogPostDeleteConfirmBinding.inflate(context.layoutInflater) }

    private val dialog = Dialog(context.requireContext())
    private lateinit var listener: PostDeleteConfirmListener

    interface PostDeleteConfirmListener {
        fun confirmPostDelete(postId: Int)
    }

    fun setOnClickListener(listener: (Int) -> Unit) {
        this.listener = object: PostDeleteConfirmListener {
            override fun confirmPostDelete(postId: Int) {
                listener(postId)
            }
        }
    }

    fun show(postId: Int) {
        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(context.requireContext(), com.marastro.mykkumi.analytics.R.string.delete_post_dialog))

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게
        dialog.setContentView(binding.root)

        binding.textBtnPostDeleteConfirm.setOnClickListener(View.OnClickListener {
            listener.confirmPostDelete(postId)
            dialog.dismiss()
        })

        binding.textBtnPostDeleteCancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }
}