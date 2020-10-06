package com.kleckus.mynotes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kleckus.mynotes.R
import com.kleckus.mynotes.system.Book
import com.kleckus.mynotes.system.MASTER_BOOK_ID
import com.kleckus.mynotes.system.MyNotesSystem
import com.kleckus.mynotes.system.Note
import kotlinx.android.synthetic.main.note_or_book_item_layout.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.VH>(){

    var onBookClicked : (bookId : Int) -> Unit = {}
    var onNoteClicked : (noteId : Int) -> Unit = {}
    var onNoteLockClicked : (noteId : Int) -> Unit = {}
    var onBookLockClicked : (bookId : Int) -> Unit = {}

    private var bookContent = mutableListOf<Book>()
    private var noteContent = mutableListOf<Note>()

    fun setContentByBookId(bookId : Int){
        bookContent.clear()
        noteContent.clear()
        val masterBook = MyNotesSystem.accessMasterBook()
        if(bookId == MASTER_BOOK_ID){
            bookContent.addAll(masterBook.bookList)
            noteContent.addAll(masterBook.noteList)
        }
        else { noteContent.addAll((MyNotesSystem.getItemById(bookId) as Book).noteList) }
    }

    class VH (itemView : View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_or_book_item_layout, parent,false)
        return VH(v)
    }

    override fun getItemCount(): Int = bookContent.size + noteContent.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val bookListSize = bookContent.size
        val itemView = holder.itemView

        if(position < bookListSize){
            // Handle access to the books
            val currentBook = bookContent[position]

            // Handling UI
            itemView.title.text = currentBook.title
            itemView.description.text = "Number of notes in this book: ${currentBook.numberOfNotes()}"
            if(currentBook.isLocked){
                itemView.boolLockIcon.setImageResource(R.drawable.locked_icon)
            }

            // Handling Clicking
            itemView.setOnClickListener { onBookClicked(currentBook.id) }
            itemView.boolLockIcon.setOnClickListener{ onBookLockClicked(currentBook.id) }
        }
        else{
            val notePosition = position - bookListSize
            // Handle access to the notes
            val currentNote = noteContent[notePosition]

            // Handling UI
            itemView.title.text = currentNote.title
            itemView.description.text = "Number of letters in this note: ${currentNote.content.length}"
            if(currentNote.isLocked){
                itemView.boolLockIcon.setImageResource(R.drawable.locked_icon)
            }

            // Handling Clicking
            itemView.setOnClickListener { onNoteClicked(currentNote.id) }
            itemView.boolLockIcon.setOnClickListener{ onNoteLockClicked(currentNote.id) }
        }
    }

}