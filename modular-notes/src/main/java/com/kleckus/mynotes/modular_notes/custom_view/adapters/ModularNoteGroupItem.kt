package com.kleckus.mynotes.modular_notes.custom_view.adapters

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import com.kleckus.mynotes.dialog_creator.service.DialogService
import com.kleckus.mynotes.domain.models.CheckListItem
import com.kleckus.mynotes.domain.models.ModularItem
import com.kleckus.mynotes.domain.models.ModularItem.CheckList
import com.kleckus.mynotes.domain.models.ModularItem.Text
import com.kleckus.mynotes.modular_notes.R
import com.kleckus.mynotes.modular_notes.onTextChange
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.create_checklist_item_dialog.view.*
import kotlinx.android.synthetic.main.layout_modular_note_item.view.*
import kotlinx.android.synthetic.main.layout_modular_note_item.view.addButton

class ModularNoteGroupItem(
    private val dialogService: DialogService,
    private val item : ModularItem,
    private val onDelete : (item : ModularItem) -> Unit
) : Item<GroupieViewHolder>() {

    private val selectedChecklistItems = mutableListOf<CheckListItem>()
    private val checklistAdapter by lazy { GroupAdapter<GroupieViewHolder>() }

    override fun getLayout() = R.layout.layout_modular_note_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            when(item){
                is Text -> setTextView()
                is CheckList -> setChecklistView()
            }
        }
    }

    private fun View.setTextView(){
        (item as Text).let{
            checkListView.isGone = true
            textView.isGone = false
            addButton.setOnClickListener(null)
            deleteButton.setOnClickListener(null)

            textTitle.text = item.title
            textInput.setText(item.content)
            textInput.onTextChange { item.content = it }
            deleteTextButton.setOnClickListener { onDelete(item) }
        }
    }

    private fun View.setChecklistView(){
        (item as CheckList).let{
            checkListView.isGone = false
            textView.isGone = true
            deleteTextButton.setOnClickListener(null)

            checkListTitle.text = item.title

            updateAdapter()
            checkListRecyclerView.adapter = checklistAdapter

            addButton.setOnClickListener {
                dialogService.create(
                    context,
                    R.layout.create_checklist_item_dialog
                ) { setupDialog(it) }
            }

            deleteButton.apply {
                isGone = false
                when{
                    selectedChecklistItems.isNotEmpty() -> setOnClickListener { deleteSelectedChecklistItems() }
                    item.checkListItems.isEmpty() -> setOnClickListener { onDelete(item) }
                    else -> {
                        setOnClickListener(null)
                        isGone = true
                    }
                }
            }
        }
    }

    private fun deleteSelectedChecklistItems(){
        (item as CheckList).let {
            selectedChecklistItems.apply {
                val editableList = item.checkListItems.toMutableList()
                forEach {
                    editableList.remove(it)
                }
                item.checkListItems = editableList.toList()
                clear()
            }
        }

        updateAdapter()
    }

    private fun handleChecklistSelection(selected : CheckListItem){
        selectedChecklistItems.apply {
            if(contains(selected)) remove(selected)
            else add(selected)
        }

        updateAdapter()
    }

    private fun View.setupDialog(dialog : AlertDialog){
        addButton.setOnClickListener {
            val title = titleField.text
            if(!title.isNullOrBlank()) {
                (item as CheckList).addCheckListItem(title.toString())
                dialog.dismiss()
            } else
                Toast.makeText(dialog.context, context.getString(R.string.empty_title_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun CheckList.addCheckListItem(field : String){
        val checkItem = CheckListItem(field, false)
        val updatedList = checkListItems.toMutableList()
        updatedList.add(checkItem)
        checkListItems = updatedList

        updateAdapter()
    }

    private fun updateAdapter(){
        (item as CheckList).let {
            checklistAdapter.apply {
                clear()
                item.checkListItems.forEach { checkItem ->
                    add(CheckListGroupItem(checkItem, ::handleChecklistSelection))
                }
            }
        }
    }
}