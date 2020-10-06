package com.kleckus.mynotes.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.kleckus.mynotes.R
import kotlinx.android.synthetic.main.create_validade_password_dialog_layout.view.*

private const val LOCK_STRING = "Lock"
private const val UNLOCK_STRING = "Unlock"

class CVPasswordDialog(private val isLocking : Boolean) : DialogFragment() {

    var onFinish : (password : String) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater!!.inflate(R.layout.create_validade_password_dialog_layout, null)

        if(isLocking) {
            view.validatingPasswordView.visibility = View.GONE
            view.doneButton.text = LOCK_STRING
        }
        else {
            view.creatingPasswordView.visibility = View.GONE
            view.doneButton.text = UNLOCK_STRING
        }

        view.doneButton.setOnClickListener {
            if(isLocking) {
                val createdPassword = view.inputCreatePassword.text.toString()
                val passwordConfirmation = view.inputConfirmPassword.text.toString()
                onCreating(createdPassword, passwordConfirmation)
            }
            else{
                val password = view.inputValidatePassword.text.toString()
                onValidating(password)
            }
        }

        builder.setView(view)
        return builder.create()
    }

    private fun onCreating(passwordOne: String, passwordTwo: String){
        if(passwordOne == passwordTwo){
            onFinish(passwordOne)
        }
    }

    private fun onValidating(password : String){
        onFinish(password)
    }
}