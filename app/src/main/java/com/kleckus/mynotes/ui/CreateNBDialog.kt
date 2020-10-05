package com.kleckus.mynotes.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.kleckus.mynotes.R
import com.kleckus.mynotes.system.Book
import com.kleckus.mynotes.system.MyNotesSystem
import com.kleckus.mynotes.system.Note
import kotlinx.android.synthetic.main.add_note_or_book_dialog_layout.view.*

class CreateNBDialog(private val ownerId : Int) : DialogFragment() {

    var onFinished :  (product : Any) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater!!.inflate(R.layout.add_note_or_book_dialog_layout, null)

        view.doneButton.setOnClickListener {
            val readTitle = view.titleInput.text.toString()
            val bookRBState = view.bookRadioButton.isChecked
            onDone(readTitle,bookRBState)
        }

        builder.setView(view)
        return builder.create()
    }

    private fun onDone(title : String, bookRBState : Boolean){
        val currentHighestId = MyNotesSystem.masterBook.highestId
        if(bookRBState){
            val book = Book(currentHighestId+1, false, "", title, mutableListOf())
            onFinished(book)
        }
        else{
            val note = Note(currentHighestId+1, ownerId, false, "", title, "")
            onFinished(note)
        }

        dismiss()
    }
}