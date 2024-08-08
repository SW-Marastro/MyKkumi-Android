package com.swmarastro.mykkumi.feature.post.imageWithPin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.swmarastro.mykkumi.common_ui.base.BaseBottomSheetFragment
import com.swmarastro.mykkumi.feature.post.R
import com.swmarastro.mykkumi.feature.post.databinding.FragmentInputProductInfoBottomSheetBinding


class InputProductInfoBottomSheet : BaseBottomSheetFragment<FragmentInputProductInfoBottomSheetBinding>(R.layout.fragment_input_product_info_bottom_sheet) {

    final val MAX_PRODUCT_NAME_LENGTH = 20

    private var inputProductInfoListener: InputProductInfoListener? = null
    private var productName: String = ""
    private var productUrl: String = ""

    interface InputProductInfoListener {
        fun submitProductInput(productName: String, productUrl: String?) // 제품 정보 입력 완료
    }

    fun setListener(inputProductInfoListener: InputProductInfoListener) {
        this.inputProductInfoListener = inputProductInfoListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 제품명 글자 수 제한
        binding.edittextInputProductName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.isNullOrEmpty() && s.length > MAX_PRODUCT_NAME_LENGTH) {
                    binding.edittextInputProductName.setText(s.subSequence(0, MAX_PRODUCT_NAME_LENGTH)) // 20자 넘어가는 건 자르기
                    binding.edittextInputProductName.setSelection(MAX_PRODUCT_NAME_LENGTH) // 커서를 맨 뒤로
                    Toast.makeText(requireContext(), getString(R.string.notice_product_name_max_length), Toast.LENGTH_SHORT).show()
                }
            }
        })

        // 입력 취소
        binding.btnConfirmCancel.setOnClickListener {
            dismiss()
        }

        // 입력 완료
        binding.btnConfirmAgree.setOnClickListener {
            val inputProductName: String = binding.edittextInputProductName.text.toString()
            var inputProductUrl: String? = binding.edittextInputProductUrl.text.toString()

            if(inputProductUrl?.length == 0) inputProductUrl = null
            if(inputProductName.isEmpty()) Toast.makeText(requireContext(), getString(R.string.notice_product_name_not_null), Toast.LENGTH_SHORT).show()
            else {
                inputProductInfoListener?.submitProductInput(inputProductName, inputProductUrl)
                dismiss()
            }
        }
    }

    override suspend fun initView() {
        // 수정하는 경우를 위해 이전 입력값 넣어주기
        productName = arguments?.getString("productName") ?: ""
        productUrl = arguments?.getString("productUrl") ?: ""

        binding.edittextInputProductName.setText(productName)
        binding.edittextInputProductUrl.setText(productUrl)
    }
}