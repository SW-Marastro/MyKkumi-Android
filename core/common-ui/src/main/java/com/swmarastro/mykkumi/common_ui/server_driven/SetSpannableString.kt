package com.swmarastro.mykkumi.common_ui.server_driven

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.swmarastro.mykkumi.domain.entity.RichText

class SetSpannableString(richText: RichText): SpannableString(richText.text) {
    class Builder(private val richText: RichText) {
        private var spannableString = SpannableString(richText.text)

        private fun setTextRange(span: Any?) {
            spannableString.setSpan(
                span,
                0,
                richText.text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        fun setColor(color: String?): Builder {
            color?.let {
                val span = ForegroundColorSpan(Color.parseColor(it))
                setTextRange(span)
            }

            return this
        }

        fun setClickable(linkUrl: String?, navController: NavController?): Builder {
            linkUrl?.let {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        navController?.navigate(deepLink = it.toUri())
                        Log.d("test", "click: ${linkUrl}")
                    }
                }
                setTextRange(clickableSpan)
            }

            return this
        }

        fun build() = spannableString
    }
}