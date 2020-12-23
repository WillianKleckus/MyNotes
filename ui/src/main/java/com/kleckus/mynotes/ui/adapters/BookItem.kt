package com.kleckus.mynotes.ui.adapters

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.dialogs.LockItemDialog
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.note_or_book_item_layout.view.*

class BookItem(
    private val book : Book,
    private val onBookClicked : (book: Book) -> Unit,
    private val onBookLocked : (book: Book, password : String?) -> Unit
) : Item<GroupieViewHolder>() {
    private companion object{
        const val BOOK_TYPE = "Book"
    }

    override fun getLayout() = R.layout.note_or_book_item_layout

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            itemType.text = BOOK_TYPE
            title.text = book.title
            description.text = "Number of notes: ${book.numberOfNotes()}"

            if(book.isLocked){
                boolLockIcon.setImageDrawable(getDrawable(context, R.drawable.locked_icon))
            } else{
                boolLockIcon.setImageDrawable(getDrawable(context, R.drawable.unlocked_icon))
            }

            boolLockIcon.setOnClickListener {
                // TODO - lock book
            }
            setOnClickListener { onBookClicked(book) }
        }
    }
}