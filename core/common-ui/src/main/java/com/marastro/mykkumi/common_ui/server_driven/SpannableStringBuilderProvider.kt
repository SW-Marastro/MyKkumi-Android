package com.marastro.mykkumi.common_ui.server_driven

import android.text.SpannableStringBuilder
import androidx.navigation.NavController
import com.marastro.mykkumi.domain.entity.RichText

class SpannableStringBuilderProvider {
    companion object {
        fun getSpannableStringBuilder(richTextList: List<RichText>, navController: NavController?): SpannableStringBuilder {
            val spannableStringBuilder = SpannableStringBuilder()

            richTextList.forEach { richText ->
                val spannableString = SetSpannableString.Builder(richText)
                    .setColor(richText.color)
                    .setClickable(richText.linkUrl, navController)
                    .build()

                spannableStringBuilder.append(spannableString)
            }

            return spannableStringBuilder
        }
    }
}