package com.kleckus.mynotes.modular_notes.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Item
import com.kleckus.mynotes.domain.models.Item.*
import com.kleckus.mynotes.domain.models.ModularItem
import com.kleckus.mynotes.domain.models.ModularItem.Text
import com.kleckus.mynotes.modular_notes.R
import com.kleckus.mynotes.modular_notes.custom_view.adapters.ModularNoteGroupItem
import com.kleckus.mynotes.modular_notes.custom_view.dialogs.CreateModuleDialog
import com.kleckus.mynotes.modular_notes.custom_view.dialogs.CreateModuleDialog.CreationType
import com.kleckus.mynotes.modular_notes.custom_view.dialogs.CreateModuleDialog.CreationType.CheckList
import com.kleckus.mynotes.modular_notes.custom_view.dialogs.CreateModuleDialog.CreationType.TextView
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
        addModuleButton.setOnClickListener{ openCreationDialog() }
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var currentNote : Note? = null

    fun applyNote(note : Note){
        currentNote = note
        updateAdapter()
    }

    private fun updateAdapter(){
        adapter.clear()
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

    private fun openCreationDialog(){
        CreateModuleDialog.openDialog(context, ::addNewItem)
    }

    private fun addNewItem(type : CreationType){
        currentNote?.let { note ->
            when(type){
                TextView -> {
                    val mutList = note.items.toMutableList()
                    mutList.add(Text(""))
                    note.items = mutList.toList()

                    currentNote = note
                }
                CheckList -> {
                    val mutList = note.items.toMutableList()
                    mutList.add(ModularItem.CheckList(listOf()))
                    note.items = mutList.toList()

                    currentNote = note
                }
            }
            updateAdapter()
        }
    }
}