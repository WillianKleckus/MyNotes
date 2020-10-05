package com.kleckus.mynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kleckus.mynotes.R
import com.kleckus.mynotes.system.MyNotesSystem.Companion.getNoteById
import com.kleckus.mynotes.system.Note
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    lateinit var note : Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        note = getNoteById(intent.extras?.getInt("id")!!)
        refreshUI()

        backButton.setOnClickListener { finish() }
    }

    private fun refreshUI(){
        noteTitleTv.text = note.title
        textInput.setText(note.content)
    }
}