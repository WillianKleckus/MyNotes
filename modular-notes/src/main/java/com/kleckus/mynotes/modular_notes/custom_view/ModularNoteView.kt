package com.kleckus.mynotes.modular_notes.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Item.*
import com.kleckus.mynotes.modular_notes.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.layout_modular_note_view.view.*

class ModularNoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_modular_note_view, this)
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var currentNote : Note? = null

    fun applyNote(note : Note){
        currentNote = note
        currentNote?.items?.forEach { item ->
            adapter.add(ModularNoteGroupItem(item))
        }
        modularRecycler.adapter = adapter
    }

    fun onDoneClicked( onDone : (note : Note) -> Unit){
        doneButton.setOnClickListener {
            currentNote?.let { note -> onDone(note) }
                ?: throw MyNotesErrors.NullNoteReference
        }
    }
}