package com.kleckus.mynotes.dialog_creator.internal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kleckus.mynotes.dialog_creator.service.DialogService

internal class DialogCreator : DialogService{
    override fun create(context : Context, resId: Int, onView: View.(dialog : AlertDialog) -> Unit) {
        val view = getView(context, resId)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .show()
        view.onView(dialog)
    }

    private fun getView(context : Context, resId: Int)
            = LayoutInflater.from(context).inflate(resId, null)

}