package com.swmarastro.mykkumi.feature.home.report

import android.os.Bundle
import android.view.View
import com.swmarastro.mykkumi.common_ui.base.BaseBottomSheetFragment
import com.swmarastro.mykkumi.feature.home.R
import com.swmarastro.mykkumi.feature.home.databinding.FragmentChooseReportBottomSheetBinding

class ChooseReportBottomSheet : BaseBottomSheetFragment<FragmentChooseReportBottomSheetBinding>(R.layout.fragment_choose_report_bottom_sheet) {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override suspend fun initView() {
        writerUuid = arguments?.getString("writerUuid")
        postId = arguments?.getInt("postId")
    }
}