package com.kleckus.mynotes.modular_notes.custom_view.adapters

import com.kleckus.mynotes.domain.models.CheckListItem
import com.kleckus.mynotes.modular_notes.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.layout_checklist_item.view.*

class CheckListGroupItem(
    private val item : CheckListItem,
    private val handleSelection : (item : CheckListItem) -> Unit
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.layout_checklist_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            option.isChecked = item.isComplete
            option.text = item.name

            option.setOnClickListener { item.toggleCheck() }
            option.setOnLongClickListener {
                handleSelection(item)
                true
            }
        }
    }

    private fun CheckListItem.toggleCheck(){
        isComplete = !isComplete
        notifyChanged()
    }
}