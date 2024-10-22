package com.marastro.mykkumi.feature.post.imageWithPin

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.concat
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseBottomSheetFragment
import com.marastro.mykkumi.feature.post.R
import com.marastro.mykkumi.common_ui.R as StringR
import com.marastro.mykkumi.feature.post.databinding.FragmentInputProductInfoBottomSheetBinding
import kotlin.math.max


class InputProductInfoBottomSheet : BaseBottomSheetFragment<FragmentInputProductInfoBottomSheetBinding>(R.layout.fragment_input_product_info_bottom_sheet) {

    private lateinit var analyticsHelper: AnalyticsHelper

    private final val MAX_PRODUCT_NAME_LENGTH = 20

    private var inputProductInfoListener: InputProductInfoListener? = null
    private var position: Int? = null
    private var productName: String = ""
    private var productUrl: String = ""

    interface InputProductInfoListener {
        fun submitProductInput(productName: String, productUrl: String?) // 제품 정보 입력 완료
        fun updateProductInput(position: Int, productName: String, productUrl: String?) // 제품 정보 수정
    }

    fun setListener(inputProductInfoListener: InputProductInfoListener) {
        this.inputProductInfoListener = inputProductInfoListener
    }

    fun setAnalyticsHelper(analyticsHelper: AnalyticsHelper) {
        this.analyticsHelper = analyticsHelper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.input_product_screen))

        // 제품명 글자 수 제한
        binding.edittextInputProductName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.isNullOrEmpty() && s.length > MAX_PRODUCT_NAME_LENGTH) {
                    // 20자 넘어가는 건 자르기 = 방금 입력된 문자
                    if(before == 0) binding.edittextInputProductName.setText(concat(s.subSequence(0, start), s.subSequence(start + count, s.length)))
                    else binding.edittextInputProductName.setText(concat(s.subSequence(0, before), s.subSequence(count, s.length)))

                    binding.edittextInputProductName.setSelection(max(start, before)) // 커서를 입력하고 있던 곳에
                    Toast.makeText(requireContext(), getString(StringR.string.notice_product_name_max_length), Toast.LENGTH_SHORT).show()
                }

                // 제품명이 입력되지 않았거나 수정사항이 없을 경우 등록 버튼 비활성화
                if(s.isNullOrEmpty() || (productName == binding.edittextInputProductName.text.toString() && productUrl == binding.edittextInputProductUrl.text.toString())) {
                    binding.btnConfirmAgree.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_btn_round12_neutral50)
                    binding.btnConfirmAgree.setTextColor(ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.neutral_300))
                }
                else {
                    binding.btnConfirmAgree.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_btn_round12_primary)
                    binding.btnConfirmAgree.setTextColor(ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.white))
                }
            }
        })

        // 구매처 입력
        binding.edittextInputProductUrl.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 수정사항이 없을 경우 등록 버튼 비활성화
                if(productName == binding.edittextInputProductName.text.toString() && productUrl == binding.edittextInputProductUrl.text.toString()) {
                    binding.btnConfirmAgree.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_btn_round12_neutral50)
                    binding.btnConfirmAgree.setTextColor(ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.neutral_300))
                }
                // 수정사항이 있고, 제품명이 있는 경우 활성화
                else if(binding.edittextInputProductName.text.isNotEmpty()) {
                    binding.btnConfirmAgree.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_btn_round12_primary)
                    binding.btnConfirmAgree.setTextColor(ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.white))
                }
            }
        })

        // 제품명: 이어서 입력할 때 커서가 맨 뒤로 배치되도록 + 커서가 위치하고 있을 때 강조
        binding.edittextInputProductName.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                binding.edittextInputProductName.text?.let {
                    binding.edittextInputProductName.setSelection(it.length)
                }
                binding.edittextInputProductName.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_input_border12_neutral800)
            }
            else {
                binding.edittextInputProductName.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_input_border12_neutral200)
            }
        }

        // 구매처: 이어서 입력할 때 커서가 맨 뒤로 배치되도록 + 커서가 위치하고 있을 때 강조
        binding.edittextInputProductUrl.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.edittextInputProductUrl.text?.let {
                    binding.edittextInputProductUrl.setSelection(it.length)
                }
                binding.edittextInputProductUrl.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_input_border12_neutral800)
            }
            else {
                binding.edittextInputProductUrl.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_input_border12_neutral200)
            }
        }

        // 입력 취소
        binding.btnConfirmCancel.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        // 입력 완료
        binding.btnConfirmAgree.setOnClickListener(View.OnClickListener {
            val inputProductName: String = binding.edittextInputProductName.text.toString()
            var inputProductUrl: String? = binding.edittextInputProductUrl.text.toString()

            if(inputProductUrl?.length == 0) inputProductUrl = null
            if(inputProductName.isEmpty()) Toast.makeText(requireContext(), getString(StringR.string.notice_product_name_not_null), Toast.LENGTH_SHORT).show()
            else {
                // 핀 내용 수정
                if(position != null) {
                    inputProductInfoListener?.updateProductInput(position!!, inputProductName, inputProductUrl)
                    dismiss()
                }

                // 새로운 핀 추가
                else {
                    inputProductInfoListener?.submitProductInput(inputProductName, inputProductUrl)
                    dismiss()
                }
            }
        })
    }

    override suspend fun initView() {
        // 수정하는 경우를 위해 이전 입력값 넣어주기
        position = arguments?.getInt("position")
        if(position == -1) position = null
        productName = arguments?.getString("productName") ?: ""
        productUrl = arguments?.getString("productUrl") ?: ""

        binding.edittextInputProductName.setText(productName)
        binding.edittextInputProductUrl.setText(productUrl)
    }
}