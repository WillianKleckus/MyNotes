package com.kleckus.mynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleckus.mynotes.R
import com.kleckus.mynotes.system.MASTER_BOOK_ID
import com.kleckus.mynotes.system.MyNotesSystem
import com.kleckus.mynotes.system.Util.Companion.navigateTo
import kotlinx.android.synthetic.main.activity_main.*

private const val CREATE_NB_TAG = "create_note_or_book_tag"

class MainActivity : AppCompatActivity() {

    private var currentOpenBookId = MASTER_BOOK_ID
    private val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyNotesSystem.initSystem().onComplete { success -> setupRecyclerView() }
        addButton.setOnClickListener { onClickAddButton(currentOpenBookId) }
        backButton.setOnClickListener { showMasterBook() }
    }

    private fun refreshUI(){
        adapter.setContentByBookId(currentOpenBookId)
        adapter.notifyDataSetChanged()
        if(currentOpenBookId == MASTER_BOOK_ID) dismissBookOptions()
        else {
            bookTitleTV.text = MyNotesSystem.getBookById(currentOpenBookId).title
            showBookOptions()
        }
    }

    private fun showBookOptions(){ bookOptionsLayout.visibility = View.VISIBLE }

    private fun dismissBookOptions(){ bookOptionsLayout.visibility = View.GONE }

    private fun showMasterBook(){
        currentOpenBookId = MASTER_BOOK_ID
        adapter.setContentByBookId(MASTER_BOOK_ID)
        refreshUI()
    }

    private fun setupRecyclerView(){
        mainRecyclerView.adapter = adapter
        adapter.onBookClicked = ::onClickBookOpt
        adapter.onNoteClicked = ::onClickNoteOpt

        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setContentByBookId(MASTER_BOOK_ID)
        refreshUI()
    }

    private fun onClickAddButton(ownerId : Int){
        val dialog = CreateNBDialog(ownerId)
        dialog.onFinished = ::onFinishCreating
        dialog.show(supportFragmentManager, CREATE_NB_TAG)
    }

    private fun onClickBookOpt(bookId : Int){
        currentOpenBookId = bookId
        adapter.setContentByBookId(currentOpenBookId)
        refreshUI()
    }

    private fun onClickNoteOpt(noteId : Int){
        navigateTo(this, NoteActivity::class.java, passId = noteId)
    }

    private fun <NoteOrBook> onFinishCreating(product : NoteOrBook){
        MyNotesSystem.createNoteOrBook(product).onComplete { refreshUI() }
    }
}