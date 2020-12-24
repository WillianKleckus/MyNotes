package com.kleckus.mynotes.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kleckus.mynotes.ui.R
import kotlinx.android.synthetic.main.confirmation_dialog_layout.view.*

object ConfirmationDialog {
    fun openDialog(
        context: Context,
        onConfirmation : (hasConfirmed : Boolean) -> Unit
    ){
        val view = getView(context)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .show()
        view.setupView(onConfirmation, dialog)
    }

    private fun View.setupView(
        onConfirmation : (hasConfirmed : Boolean) -> Unit,
        dialog: AlertDialog
    ) {
        yesButton.setOnClickListener {
            onConfirmation(true)
            dialog.dismiss()
        }
        noButton.setOnClickListener {
            onConfirmation(false)
            dialog.dismiss()
        }
    }

    private fun getView(context : Context)
            = LayoutInflater.from(context).inflate(R.layout.confirmation_dialog_layout, null)

}