package com.marastro.mykkumi.common_ui.post

import android.os.Bundle
import android.view.View
import com.marastro.mykkumi.common_ui.base.BaseBottomSheetFragment
import com.marastro.mykkumi.common_ui.databinding.FragmentViewProductInfoBottomSheetBinding
import com.marastro.mykkumi.common_ui.R


class ViewProductInfoBottomSheet : BaseBottomSheetFragment<FragmentViewProductInfoBottomSheetBinding>(R.layout.fragment_view_product_info_bottom_sheet) {

    private var productName: String = ""
    private var productUrl: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 구매처 없는 경우 숨기기
        if(productUrl.isEmpty()) {
            binding.textInputProductUrlLabel.visibility = View.GONE
            binding.textInputProductUrl.visibility = View.GONE
        }
        else {
            binding.textInputProductUrlLabel.visibility = View.VISIBLE
            binding.textInputProductUrl.visibility = View.VISIBLE
        }

        // 닫기버튼
        binding.btnClose.setOnClickListener(View.OnClickListener {
            dismiss()
        })
    }

    override suspend fun initView() {
        productName = arguments?.getString("productName") ?: ""
        productUrl = arguments?.getString("productUrl") ?: ""

        binding.textInputProductName.setText(productName)
        binding.textInputProductUrl.setText(productUrl)
    }
}