package com.kleckus.mynotes.dialog_creator.service

import android.content.Context
import androidx.annotation.StringRes
import com.kleckus.mynotes.dialog_creator.R

interface YesOrNoDialog {
    fun create(
        context : Context,
        @StringRes title : Int = R.string.are_you_sure_string,
        onConfirm : (option : Boolean) -> Unit
    )
}