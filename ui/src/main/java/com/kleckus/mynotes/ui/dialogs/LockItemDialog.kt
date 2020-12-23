package com.kleckus.mynotes.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.dialogs.LockItemDialog.ErrorMessages.PASSWORDS_DONT_MATCH
import com.kleckus.mynotes.ui.dialogs.LockItemDialog.Validation.*
import kotlinx.android.synthetic.main.locking_dialog_layout.view.*

object LockItemDialog {
    object ErrorMessages{
        const val PASSWORDS_DONT_MATCH = "Passwords don't match"
    }

    fun openDialog(
        id : String,
        lock : (id : String, password : String) -> Unit,
        context : Context
    ){
        val view = getView(context)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .show()
        view.setupView(id, dialog, lock)
    }

    private fun View.setupView(
        id: String,
        dialog: AlertDialog,
        lock : (id : String, password : String) -> Unit
    ) {
        doneButton.setOnClickListener {
            val password = passwordField.text.toString()
            val passwordConfirmation = confirmationPasswordField.text.toString()

            when(val result = validate(password, passwordConfirmation)){
                is Valid -> {
                    lock(id, password)
                    dialog.dismiss()
                }
                is Invalid -> warningOrErrorMessage.text = result.message
            }
        }
    }

    private fun getView(context : Context)
            = LayoutInflater.from(context).inflate(R.layout.locking_dialog_layout, null)

    private fun validate(pw1 : String, pw2 : String) : Validation {
        var message : String? = null

        if(pw1 != pw2) message = PASSWORDS_DONT_MATCH

        return message?.let{ Invalid(it) } ?: Valid
    }

    sealed class Validation{
        object Valid : Validation()
        data class Invalid(val message : String) : Validation()
    }

}