package com.kleckus.mynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleckus.mynotes.R
import com.kleckus.mynotes.database.Database
import com.kleckus.mynotes.system.*
import com.kleckus.mynotes.system.Util.Companion.log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

private const val CREATE_NB_TAG = "create_note_or_book_tag"
private const val LOCK_NB_TAG = "lock_note_or_book_tag"
private const val PASSWORD_VALIDATION_TAG = "password_validation_tag"
private const val CONFIRMATION_TAG = "confirmation_tag"

private const val NO_NOTE_ID = -1

class MainActivity : AppCompatActivity() {

    private var currentOpenBookId = MASTER_BOOK_ID
    private var currentOpenNoteId : Int = NO_NOTE_ID
    private lateinit var adapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyNotesSystem.initSystem().onComplete { success ->
            setupRecyclerView()
            backButton.setOnClickListener { backButtonPressed() }
            deleteButton.setOnClickListener { deleteButtonPressed() }
            addButton.setOnClickListener { onClickAddButton(currentOpenBookId) }
            doneButton.setOnClickListener { onDoneEditingNote() }
        }
    }

    private fun refreshUI(){
        adapter.setContentByBookId(currentOpenBookId)
        adapter.notifyDataSetChanged()

        if(currentOpenNoteId != NO_NOTE_ID) {
            val openNote = MyNotesSystem.getItemById(currentOpenNoteId) as Note
            showNoteView()
            titleTV.text = openNote.title
            textInput.setText(openNote.content)
        }
        else{
            showBookView()
            titleTV.text = (MyNotesSystem.getItemById(currentOpenBookId) as Book).title
        }

        if(currentOpenNoteId == NO_NOTE_ID && currentOpenBookId == MASTER_BOOK_ID){ dismissOptions() }
        else showOptions()
    }

    private fun setupRecyclerView(){
        adapter = MainAdapter()
        mainRecyclerView.adapter = adapter
        adapter.onViewClicked = ::onItemClick
        adapter.onLockClicked = ::onLockClicked

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
        if(currentOpenNoteId != NO_NOTE_ID){
            currentOpenNoteId = NO_NOTE_ID
            showBookView()
        }
        else{
            currentOpenBookId = MASTER_BOOK_ID
            adapter.setContentByBookId(MASTER_BOOK_ID)
        }
        refreshUI()
    }

    private fun deleteButtonPressed(){
        if(currentOpenNoteId != NO_NOTE_ID) deleteValidation(currentOpenNoteId)
        else deleteValidation(currentOpenBookId)
    }

    private fun deleteValidation(itemId: Int){
        val dialog = ConfirmationDialog()
        dialog.onConfirmation =  { confirmed ->
            if(confirmed) onDeleting(itemId)
        }
        dialog.show(supportFragmentManager, CONFIRMATION_TAG)
    }

    private fun onDeleting(itemId : Int){
        MyNotesSystem.deleteItem(itemId).onComplete { returnedPair ->
            currentOpenNoteId = NO_NOTE_ID
            currentOpenBookId = returnedPair.first
            refreshUI()
        }
    }

    private fun onItemClick(itemId: Int){
        val validationItem = MyNotesSystem.getItemById(itemId) as Lockable
        if (validationItem.isLocked){
            val dialog = CVPasswordDialog(false, itemId)
            dialog.onFinish = :: openItem
            dialog.show(supportFragmentManager, PASSWORD_VALIDATION_TAG)
        }
        else{
            openItem(itemId, NO_PASSWORD)
        }
    }

    private fun openItem(itemId : Int, password : Int){
        if(password != NO_PASSWORD && !validatePassword(itemId, password)){
            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
            return
        }

        when (val item = MyNotesSystem.getItemById(itemId)) {
            is Book -> {
                currentOpenBookId = itemId
                adapter.setContentByBookId(currentOpenBookId)
                refreshUI()
            }
            is Note -> {
                currentOpenNoteId = itemId
                showNoteView()
                refreshUI()
            }
            else -> {
                log("Something went wrong")
            }
        }
    }

    // Book and Note window button handlers --------------------------------------------------------
    private fun onLockClicked(itemId : Int){
        val item = MyNotesSystem.getItemById(itemId) as Lockable
        val isLocking = !item.isLocked
        val dialog = CVPasswordDialog(isLocking, itemId)
        dialog.onFinish = ::onLocking
        dialog.show(supportFragmentManager, LOCK_NB_TAG)
    }

    private fun onLocking(itemId: Int, password : Int){
        MyNotesSystem.toggleLock(itemId, password).onComplete { refreshUI() }
    }

    // Book window buttons
    private fun onClickAddButton(ownerId : Int){
        val isInMasterBook = currentOpenBookId == MASTER_BOOK_ID
        val dialog = CreateNBDialog(ownerId, isInMasterBook)
        dialog.onFinished = ::onFinishCreating
        dialog.show(supportFragmentManager, CREATE_NB_TAG)
    }

    private fun <NoteOrBook> onFinishCreating(product : NoteOrBook){
        MyNotesSystem.createNoteOrBook(product).onComplete { refreshUI() }
    }

    // Note window buttons
    private fun onDoneEditingNote(){
        (MyNotesSystem.getItemById(currentOpenNoteId) as Note).content = textInput.text.toString()
        Database.saveState().onComplete { success ->
            currentOpenNoteId = NO_NOTE_ID
            showBookView()
        }
    }

    // Validation functions ------------------------------------------------------------------------
    private fun validatePassword(itemId: Int, password: Int) : Boolean{
        val lockableItem = MyNotesSystem.getItemById(itemId) as Lockable
        return !(lockableItem.isLocked && (password != lockableItem.password))
    }
}