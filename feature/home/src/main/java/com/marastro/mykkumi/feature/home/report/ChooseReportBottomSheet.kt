package com.marastro.mykkumi.feature.home.report

import android.content.Context
import android.os.Bundle
import android.view.View
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseBottomSheetFragment
import com.marastro.mykkumi.feature.home.R
import com.marastro.mykkumi.feature.home.databinding.FragmentChooseReportBottomSheetBinding

class ChooseReportBottomSheet : BaseBottomSheetFragment<FragmentChooseReportBottomSheetBinding>(R.layout.fragment_choose_report_bottom_sheet) {

    private lateinit var analyticsHelper: AnalyticsHelper

    private var chooseReportListener: ChooseReportListener? = null

    private var writerUuid: String? = null
    private var postId: Int? = null

    interface ChooseReportListener {
        fun reportPost(postId: Int)
        fun repostWriter(writerUuid: String)
    }

    fun setListener(chooseReportListener: ChooseReportListener) {
        this.chooseReportListener = chooseReportListener
    }

    fun setAnalyticsHelper(analyticsHelper: AnalyticsHelper) {
        this.analyticsHelper = analyticsHelper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.choose_report_screen))

        binding.textBtnReportPost.setOnClickListener(View.OnClickListener {
            postId?.let { postId -> chooseReportListener?.reportPost(postId) }
            dismiss()
        })

        binding.textBtnReportWriter.setOnClickListener(View.OnClickListener {
            writerUuid?.let { writerUuid -> chooseReportListener?.repostWriter(writerUuid) }
            dismiss()
        })
    }

    override suspend fun initView() {
        writerUuid = arguments?.getString("writerUuid")
        postId = arguments?.getInt("postId")
    }
}