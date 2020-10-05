package com.kleckus.mynotes.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.kleckus.mynotes.R
import kotlinx.android.synthetic.main.create_validade_password_dialog_layout.view.*

class CVPasswordDialog(private val isCreating : Boolean)  : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        var onFinish : (password : String) -> Unit = {}

        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater!!.inflate(R.layout.create_validade_password_dialog_layout, null)

        if(isCreating) view.validatingPasswordView.visibility = View.GONE
        else view.creatingPasswordView.visibility = View.GONE

        builder.setView(view)
        return builder.create()
    }
}