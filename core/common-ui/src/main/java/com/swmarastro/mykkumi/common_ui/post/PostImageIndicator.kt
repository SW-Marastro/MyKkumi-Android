package com.swmarastro.mykkumi.common_ui.post

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.swmarastro.mykkumi.common_ui.R

// 포스트 이미지 인디케이터
class PostImageIndicator: LinearLayout {
    private var mContext: Context? = null

    private var imageDot: MutableList<ImageView> = mutableListOf()

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
    }

    // indicator 생성
    fun createIndicator(count: Int, position: Int) {
        this.removeAllViews()

        for(i: Int in 0..count-1) {
            imageDot.add(
                ImageView(mContext)
                    .apply {
                        setPadding(2)
                    }
            )
            this.addView(imageDot[i])
        }

        selectDot(position)
    }

    // 선택된 페이지 indicator 표시
    fun selectDot(position: Int) {
        for(idx: Int in imageDot.indices) {
            if (idx == position)
                imageDot[idx].setImageResource(R.drawable.shape_post_image_indicator_selected)
            else
                imageDot[idx].setImageResource(R.drawable.shape_post_image_indicator_default)
        }
    }
}