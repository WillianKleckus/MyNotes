package com.kleckus.mynotes.modular_notes.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import com.kleckus.mynotes.dialog_creator.service.DialogService
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Item.*
import com.kleckus.mynotes.domain.models.ModularItem
import com.kleckus.mynotes.domain.models.ModularItem.Text
import com.kleckus.mynotes.modular_notes.R
import com.kleckus.mynotes.modular_notes.custom_view.adapters.ModularNoteGroupItem
import com.kleckus.mynotes.modular_notes.custom_view.models.CreationType
import com.kleckus.mynotes.modular_notes.custom_view.models.CreationType.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.create_module_dialog.view.*
import kotlinx.android.synthetic.main.layout_modular_note_view.view.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class ModularNoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), DIAware {

    override val di : DI by closestDI(context)

    private val dialogService by instance<DialogService>()

    init {
        View.inflate(context, R.layout.layout_modular_note_view, this)
        addModuleButton.setOnClickListener{ executeCreationDialog() }
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
            adapter.add(ModularNoteGroupItem(dialogService, item, ::deleteItem))
        }
        modularRecycler.adapter = adapter
    }

    fun onDoneClicked( onDone : (note : Note) -> Unit){
        doneButton.setOnClickListener {
            currentNote?.let { note -> onDone(note) }
                ?: throw MyNotesErrors.NullNoteReference
        }
    }

    private fun executeCreationDialog(){
        dialogService.create(
            context,
            R.layout.create_module_dialog
        ){ dialog ->
            checklistChip.setOnClickListener {
                if(textChip.isChecked) textChip.isChecked = false
            }

            textChip.setOnClickListener {
                if(checklistChip.isChecked) checklistChip.isChecked = false
            }

            addButton.setOnClickListener {
                when{
                    textChip.isChecked ->
                        addNewItem(
                            type = Text,
                            title = titleField.text.toString()
                        ).also { dialog.dismiss() }

                    checklistChip.isChecked ->
                        addNewItem(
                            type = CheckList,
                            title = titleField.text.toString()
                        ).also { dialog.dismiss() }

                    else -> Toast.makeText(context, context.getString(R.string.type_not_chosen), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addNewItem(type : CreationType, title : String){
        currentNote?.let { note ->
            when(type){
                Text -> {
                    val mutList = note.items.toMutableList()
                    mutList.add(Text(title, ""))
                    note.items = mutList.toList()

                    currentNote = note
                }
                CheckList -> {
                    val mutList = note.items.toMutableList()
                    mutList.add(ModularItem.CheckList(title, listOf()))
                    note.items = mutList.toList()

                    currentNote = note
                }
            }
            updateAdapter()
        }
    }

    private fun deleteItem(item : ModularItem){
        currentNote?.let{ note ->
            val editableList = note.items.toMutableList()
            editableList.removeAll { it == item }
            note.items = editableList.toList()

            currentNote = note
        }
        updateAdapter()
    }
}