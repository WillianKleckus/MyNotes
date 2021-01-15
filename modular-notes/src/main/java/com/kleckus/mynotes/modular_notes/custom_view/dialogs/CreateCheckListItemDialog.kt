package com.kleckus.mynotes.modular_notes.custom_view.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kleckus.mynotes.modular_notes.R
import com.kleckus.mynotes.modular_notes.custom_view.dialogs.CreateModuleDialog.CreationType.CheckList
import com.kleckus.mynotes.modular_notes.custom_view.dialogs.CreateModuleDialog.CreationType.TextView
import kotlinx.android.synthetic.main.create_module_dialog.view.*

object CreateCheckListItemDialog {
    fun openDialog(
        context: Context
    ){
        val view = getView(context)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .show()
        view.setupView(dialog)
    }

    private fun View.setupView(
        dialog: AlertDialog
    ) {

    }

    private fun getView(context : Context)
            = LayoutInflater.from(context).inflate(R.layout.create_module_dialog, null)

}