package com.kleckus.mynotes.ui.adapters

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.ui.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.note_or_book_item_layout.view.*

class NoteItem(
    private val note : Note,
    private val onNoteClicked : (note : Note) -> Unit
) : Item<GroupieViewHolder>() {
    private companion object{
        const val NOTE_TYPE = "Note"
    }

    override fun getLayout() = R.layout.note_or_book_item_layout

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            itemType.text = NOTE_TYPE
            title.text = note.title
            description.text = "Number of words: ${note.numberOfWords()}"

            if(note.isLocked){
                boolLockIcon.setImageDrawable(getDrawable(context, R.drawable.locked_icon))
            } else{
                boolLockIcon.setImageDrawable(getDrawable(context, R.drawable.unlocked_icon))
            }

            setOnClickListener { onNoteClicked(note) }
        }
    }
}