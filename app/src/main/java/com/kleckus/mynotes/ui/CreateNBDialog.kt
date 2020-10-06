package com.kleckus.mynotes.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.kleckus.mynotes.R
import com.kleckus.mynotes.system.Book
import com.kleckus.mynotes.system.MyNotesSystem
import com.kleckus.mynotes.system.NO_PASSWORD
import com.kleckus.mynotes.system.Note
import kotlinx.android.synthetic.main.add_note_or_book_dialog_layout.view.*

class CreateNBDialog(private val ownerId : Int , private val isInMasterBook : Boolean) : DialogFragment() {

    var onFinished :  (product : Any) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater!!.inflate(R.layout.add_note_or_book_dialog_layout, null)

        if(!isInMasterBook) view.noteOrBookRadioGroup.visibility = View.GONE

        view.doneButton.setOnClickListener {
            val readTitle = view.titleInput.text.toString()
            val bookRBState = view.bookRadioButton.isChecked
            onDone(readTitle,bookRBState)
        }

        builder.setView(view)
        return builder.create()
    }

    private fun onDone(title : String, bookRBState : Boolean){
        val currentHighestId = MyNotesSystem.accessMasterBook().highestId
        if(bookRBState){
            val book = Book(currentHighestId+1, false, NO_PASSWORD, title, mutableListOf())
            onFinished(book)
        }
        else{
            val note = Note(currentHighestId+1, ownerId, false, NO_PASSWORD, title, "")
            onFinished(note)
        }

        dismiss()
    }
}