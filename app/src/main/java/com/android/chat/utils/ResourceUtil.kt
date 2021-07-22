package com.android.chat.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.android.chat.utils.Chat.Companion.context

object ResourceUtil {

    fun getSolidRectDrawable(radius: Int, color: String): GradientDrawable {
        val bg = GradientDrawable()
        bg.shape = GradientDrawable.RECTANGLE
        bg.cornerRadius = Helper.dpToPx(radius).toFloat()
        bg.setColor(Color.parseColor(color))
        return bg
    }

    fun getSolidRectDrawable(radius: Int, color: Int): GradientDrawable {
        val bg = GradientDrawable()
        bg.shape = GradientDrawable.RECTANGLE
        bg.cornerRadius = Helper.dpToPx(radius).toFloat()
        bg.setColor(ContextCompat.getColor(context, color))
        return bg
    }

}