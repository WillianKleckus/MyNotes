package com.kleckus.mynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleckus.mynotes.R
import com.kleckus.mynotes.database.Database
import com.kleckus.mynotes.system.MASTER_BOOK_ID
import com.kleckus.mynotes.system.MyNotesSystem
import com.kleckus.mynotes.system.MyNotesSystem.Companion.getBookById
import com.kleckus.mynotes.system.MyNotesSystem.Companion.getNoteById
import com.kleckus.mynotes.system.Note
import com.kleckus.mynotes.system.Util.Companion.navigateTo
import kotlinx.android.synthetic.main.activity_main.*

private const val CREATE_NB_TAG = "create_note_or_book_tag"
private const val NO_NOTE_CONST = -1

class MainActivity : AppCompatActivity() {

    private var currentOpenBookId = MASTER_BOOK_ID
    private var openNoteId : Int = NO_NOTE_CONST
    private val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyNotesSystem.initSystem().onComplete { success -> setupRecyclerView() }
        addButton.setOnClickListener { onClickAddButton(currentOpenBookId) }
        backButton.setOnClickListener { backButtonPressed() }
        doneButton.setOnClickListener { onDoneEditingNote() }
    }

    private fun refreshUI(){
        adapter.setContentByBookId(currentOpenBookId)
        adapter.notifyDataSetChanged()

        if(openNoteId != NO_NOTE_CONST) {
            val openNote = getNoteById(openNoteId)
            showNoteView()
            titleTV.text = openNote.title
            textInput.setText(openNote.content)
        }
        else{
            showBookView()
            titleTV.text = getBookById(currentOpenBookId).title
        }

        if(openNoteId == NO_NOTE_CONST && currentOpenBookId == MASTER_BOOK_ID){ dismissOptions() }
        else showOptions()
    }

    private fun setupRecyclerView(){
        mainRecyclerView.adapter = adapter
        adapter.onBookClicked = ::onClickBookOpt
        adapter.onNoteClicked = ::onClickNoteOpt

        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setContentByBookId(MASTER_BOOK_ID)
        refreshUI()
    }

    // Window management ---------------------------------------------------------------------------

    private fun showOptions(){ optionsLayout.visibility = View.VISIBLE }

    private fun dismissOptions(){ optionsLayout.visibility = View.GONE }

    private fun showBookView(){
        noteView.visibility = View.GONE
        bookView.visibility = View.VISIBLE
    }

    private fun showNoteView(){
        bookView.visibility = View.GONE
        noteView.visibility = View.VISIBLE
    }

    // Button pressing management ------------------------------------------------------------------
    // Menu buttons
    private fun backButtonPressed(){
        if(openNoteId != NO_NOTE_CONST){
            openNoteId = NO_NOTE_CONST
            showBookView()
        }
        else{
            currentOpenBookId = MASTER_BOOK_ID
            adapter.setContentByBookId(MASTER_BOOK_ID)
        }
        refreshUI()
    }

    // Book window buttons
    private fun onClickAddButton(ownerId : Int){
        val dialog = CreateNBDialog(ownerId)
        dialog.onFinished = ::onFinishCreating
        dialog.show(supportFragmentManager, CREATE_NB_TAG)
    }

    private fun <NoteOrBook> onFinishCreating(product : NoteOrBook){
        MyNotesSystem.createNoteOrBook(product).onComplete { refreshUI() }
    }

    private fun onClickBookOpt(bookId : Int){
        currentOpenBookId = bookId
        adapter.setContentByBookId(currentOpenBookId)
        refreshUI()
    }

    private fun onClickNoteOpt(noteId : Int){
        openNoteId = noteId
        showNoteView()
        refreshUI()
    }

    // Note window buttons
    private fun onDoneEditingNote(){
        getNoteById(openNoteId).content = textInput.text.toString()
        Database.saveState().onComplete { success ->
            openNoteId = NO_NOTE_CONST
            showBookView()
        }
    }


}