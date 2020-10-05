package com.kleckus.mynotes.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.kleckus.mynotes.R

class CreateNBDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.add_note_or_book_dialog_layout, null)

        builder.setView(view)

        return builder.create()
    }
}