package com.kleckus.mynotes.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kleckus.mynotes.dialog_creator.service.DialogService
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.dialogs.PasswordDialog.ErrorMessages.PASSWORDS_DONT_MATCH
import com.kleckus.mynotes.ui.dialogs.PasswordDialog.Validation.*
import kotlinx.android.synthetic.main.locking_dialog_layout.view.*

object PasswordDialog {
    operator fun invoke(
        context: Context,
        dialogService: DialogService,
        id : String,
        isLocking : Boolean,
        lock : (id : String, password : String) -> Unit
    ){
        dialogService.create(
            context = context,
            resId = R.layout.locking_dialog_layout
        ){ dialog ->
            confirmationPasswordView.isGone = !isLocking
            warningOrErrorMessage.isGone = !isLocking

            doneButton.setOnClickListener {
                val password = passwordField.text.toString()
                val passwordConfirmation = confirmationPasswordField.text.toString()

                if(isLocking){
                    when(val result = validate(password, passwordConfirmation)){
                        is Valid -> {
                            lock(id, password)
                            dialog.dismiss()
                        }
                        is Invalid -> warningOrErrorMessage.text = result.message
                    }
                } else{
                    lock(id, password)
                    dialog.dismiss()
                }
            }
        }
    }

    private fun validate(pw1 : String, pw2 : String) : Validation {
        var message : String? = null

        if(pw1 != pw2) message = PASSWORDS_DONT_MATCH

        return message?.let{ Invalid(it) } ?: Valid
    }

    sealed class Validation{
        object Valid : Validation()
        data class Invalid(val message : String) : Validation()
    }

    object ErrorMessages{
        const val PASSWORDS_DONT_MATCH = "Passwords don't match"
    }
}