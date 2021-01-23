package com.kleckus.mynotes.dialog_creator.service

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog

interface DialogService {
    fun create(context : Context, @LayoutRes resId: Int, onView : View.(dialog : AlertDialog) -> Unit)
}