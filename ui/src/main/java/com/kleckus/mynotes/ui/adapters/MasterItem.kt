package com.kleckus.mynotes.ui.adapters

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.kleckus.mynotes.dialog_creator.service.DialogService
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.dialogs.PasswordDialog
import com.xwray.groupie.GroupieViewHolder
import com.kleckus.mynotes.domain.models.Item
import com.xwray.groupie.Item as GroupieItem
import kotlinx.android.synthetic.main.note_or_book_item_layout.view.*

class MasterItem(
    private val item : Item,
    private val dialogService: DialogService,
    private val onItemClicked : (item: Item) -> Unit,
    private val onItemLocked : (id: String, password : String) -> Unit
) : GroupieItem<GroupieViewHolder>() {
    private companion object{
        const val BOOK_ITEM = "Book"
        const val NOTE_ITEM = "Note"
    }

    override fun getLayout() = R.layout.note_or_book_item_layout

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            when(item){
                is Item.Book -> itemType.text = BOOK_ITEM
                is Item.Note -> itemType.text = NOTE_ITEM
            }
            title.text = item.title
            description.text = item.getDescription()

            if(item.isLocked){
                boolLockIcon.setImageDrawable(getDrawable(context, R.drawable.locked_icon))
            } else{
                boolLockIcon.setImageDrawable(getDrawable(context, R.drawable.unlocked_icon))
            }

            boolLockIcon.setOnClickListener {
                PasswordDialog(
                    context = context,
                    dialogService = dialogService,
                    id = item.id,
                    isLocking = !item.isLocked,
                    lock = onItemLocked
                )
            }
            setOnClickListener { onItemClicked(item) }
        }
    }
}