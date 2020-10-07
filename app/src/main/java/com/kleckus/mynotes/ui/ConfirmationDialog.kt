package com.kleckus.mynotes.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.kleckus.mynotes.R
import kotlinx.android.synthetic.main.confirmation_dialog_layout.view.*

class ConfirmationDialog : DialogFragment(){

    var onConfirmation : (option : Boolean) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater!!.inflate(R.layout.confirmation_dialog_layout, null)

        view.yesButton.setOnClickListener {
            onConfirmation(true)
            dismiss()
        }
        view.noButton.setOnClickListener {
            onConfirmation(false)
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}
