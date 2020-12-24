package com.kleckus.mynotes.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kleckus.mynotes.domain.Constants.MASTER_BOOK_ID
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.dialogs.CreationType.BOOK
import com.kleckus.mynotes.ui.dialogs.CreationType.NOTE
import kotlinx.android.synthetic.main.add_note_or_book_dialog.view.*

object NoteOrBookCreationDialog {
    fun openDialog(
        ownerId : String,
        create : (title: String, ownerId : String, type : CreationType) -> Unit,
        context : Context
    ){
        val view = getView(context)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .show()
        view.setupView(ownerId, dialog, create)
    }

    private fun View.setupView(
        ownerId: String,
        dialog: AlertDialog,
        create : (title: String, ownerId : String, type : CreationType) -> Unit
    ) {
        noteOrBookRadioGroup.isGone = ownerId != MASTER_BOOK_ID

        doneButton.setOnClickListener {
            if(bookRadioButton.isChecked)
                create(titleInput.text.toString(), ownerId, BOOK)
            else
                create(titleInput.text.toString(), ownerId, NOTE)
            dialog.dismiss()
        }
    }


    private fun getView(context : Context)
            = LayoutInflater.from(context).inflate(R.layout.add_note_or_book_dialog, null)
}