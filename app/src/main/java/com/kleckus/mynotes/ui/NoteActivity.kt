package com.kleckus.mynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kleckus.mynotes.R

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
    }
}