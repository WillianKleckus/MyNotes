package com.kleckus.mynotes.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.kleckus.mynotes.R
import kotlinx.android.synthetic.main.create_validade_password_dialog_layout.view.*

private const val NONE_CREATED = -1
private const val NONE_CONFIRMED = -2
private const val NONE_PASSED = -3

class CVPasswordDialog(private val isLocking : Boolean, private val itemId : Int) : DialogFragment() {

    var onFinish : (itemId : Int, password : Int) -> Unit = {_,_ ->}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater!!.inflate(R.layout.create_validade_password_dialog_layout, null)

        if(isLocking) {
            view.validatingPasswordView.visibility = View.GONE
        }
        else {
            view.creatingPasswordView.visibility = View.GONE
        }

        view.doneButton.setOnClickListener {
            if(isLocking) {
                val inputCreatedPassword = view.inputCreatePassword.text.toString()
                var createdPassword = NONE_CREATED
                if(inputCreatedPassword.isNotEmpty()) createdPassword = inputCreatedPassword.toInt()

                val inputConfirmationPassword = view.inputConfirmPassword.text.toString()
                var passwordConfirmation =  NONE_CONFIRMED
                if(inputConfirmationPassword.isNotEmpty()) passwordConfirmation = inputConfirmationPassword.toInt()

                onCreating(createdPassword, passwordConfirmation)
            }
            else{
                val inputPassword = view.inputValidatePassword.text.toString()
                var password = NONE_PASSED
                if(inputPassword.isNotEmpty()) password = inputPassword.toInt()

                onValidating(password)
            }
        }

        builder.setView(view)
        return builder.create()
    }

    private fun onCreating(passwordOne: Int, passwordTwo: Int){
        if(passwordOne == passwordTwo){
            onFinish(itemId, passwordOne)
            dismiss()
        }
        else{
            Toast.makeText(context, "Passwords don't match or fields are empty.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onValidating(password : Int){
        onFinish(itemId, password)
        dismiss()
    }
}