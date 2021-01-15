package com.kleckus.mynotes.modular_notes.custom_view.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kleckus.mynotes.modular_notes.R
import kotlinx.android.synthetic.main.create_checklist_item_dialog.view.*

object CreateCheckListItemDialog {
    fun openDialog(
        context: Context,
        onCreateCheckListItem : (field : String) -> Unit
    ){
        val view = getView(context)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .show()
        view.setupView(dialog, onCreateCheckListItem)
    }

    private fun View.setupView(
        dialog: AlertDialog,
        onCreateCheckListItem : (field : String) -> Unit
    ) {
        addButton.setOnClickListener {
            val title = titleField.text
            if(!title.isNullOrBlank()) {
                onCreateCheckListItem(title.toString())
                dialog.dismiss()
            } else
                Toast.makeText(dialog.context, EMPTY_OR_NULL_TITLE, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getView(context : Context)
            = LayoutInflater.from(context).inflate(R.layout.create_checklist_item_dialog, null)

    private const val EMPTY_OR_NULL_TITLE = "Title cannot be empty"

}