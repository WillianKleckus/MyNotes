package com.kleckus.mynotes.modular_notes.custom_view

import com.kleckus.mynotes.domain.models.CheckListItem
import com.kleckus.mynotes.modular_notes.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.layout_checklist_item.view.*

class CheckListGroupItem(
    val item : CheckListItem
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.layout_modular_note_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            option.isChecked = item.isComplete
            option.text = item.name

            option.setOnClickListener { item.toggleCheck() }
            option.setOnLongClickListener { TODO() }
        }
    }

    private fun CheckListItem.toggleCheck(){
        isComplete = !isComplete
        notifyChanged()
    }
}