package me.dannly.core.util

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {

    data class DynamicString(val text: String): UiText()
    data class StringResource(@StringRes val resId: Int): UiText()

    fun asString(context: Context) = when(this) {
        is DynamicString -> text
        is StringResource -> context.getString(resId)
    }
}