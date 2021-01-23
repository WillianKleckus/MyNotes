package com.kleckus.mynotes.dialog_creator.service

import android.content.Context

interface YesOrNoDialog {
    fun create(context : Context, onConfirm : (option : Boolean) -> Unit)
}