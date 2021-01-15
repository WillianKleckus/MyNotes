package com.kleckus.mynotes.modular_notes.custom_view.adapters

import androidx.core.view.isGone
import com.kleckus.mynotes.domain.models.CheckListItem
import com.kleckus.mynotes.domain.models.ModularItem
import com.kleckus.mynotes.domain.models.ModularItem.CheckList
import com.kleckus.mynotes.domain.models.ModularItem.Text
import com.kleckus.mynotes.modular_notes.R
import com.kleckus.mynotes.modular_notes.custom_view.dialogs.CreateCheckListItemDialog
import com.kleckus.mynotes.modular_notes.onTextChange
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.layout_modular_note_item.view.*

class ModularNoteGroupItem(
    private val item : ModularItem
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.layout_modular_note_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            when(item){
                is Text -> {
                    checkListView.isGone = true
                    textView.isGone = false

                    textTitle.text = item.title
                    textInput.setText(item.content)
                    textInput.onTextChange { item.content = it }
                }
                is CheckList -> {
                    checkListView.isGone = false
                    textView.isGone = true

                    checkListTitle.text = item.title

                    val adapter = GroupAdapter<GroupieViewHolder>()
                    item.checkListItems.forEach { checkItem ->
                        adapter.add(CheckListGroupItem(checkItem))
                    }
                    checkListRecyclerView.adapter = adapter

                    addButton.setOnClickListener {
                        CreateCheckListItemDialog.openDialog(context) { item.addCheckListItem(it) }
                    }
                }
            }
        }
    }

    private fun CheckList.addCheckListItem(field : String){
        val checkItem = CheckListItem(field, false)
        val updatedList = checkListItems.toMutableList()
        updatedList.add(checkItem)
        checkListItems = updatedList
        notifyChanged()
    }
}