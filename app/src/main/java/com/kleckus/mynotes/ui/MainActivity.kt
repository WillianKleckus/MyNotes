package com.kleckus.mynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleckus.mynotes.R
import com.kleckus.mynotes.system.MyNotesSystem
import kotlinx.android.synthetic.main.activity_main.*

private const val CREATE_NB_TAG = "create_note_or_book_tag"

class MainActivity : AppCompatActivity() {
    companion object {
        fun refreshUI(){ adapter.notifyDataSetChanged() }
        private val adapter = MainAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyNotesSystem.initSystem().onComplete { success ->
            setupRecyclerView()
            addButton.setOnClickListener { onClickAddButton() }
        }
    }

    private fun setupRecyclerView(){
        mainRecyclerView.adapter = adapter
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setContents(MyNotesSystem.bookList, MyNotesSystem.noteList)
    }

    private fun onClickAddButton(){
        val dialog = CreateNBDialog()
        dialog.onFinished = ::onFinishCreating
        dialog.show(supportFragmentManager, CREATE_NB_TAG)
    }

    private fun <NoteOrBook> onFinishCreating(product : NoteOrBook){
        MyNotesSystem.createNoteOrBook(product).onComplete { refreshUI() }
    }
}