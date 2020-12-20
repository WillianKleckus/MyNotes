package com.kleckus.mynotes.ui.master

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cafe.adriel.dalek.*
import com.kleckus.mynotes.domain.Constants.MASTER_BOOK_ID
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.adapters.BookItem
import com.kleckus.mynotes.ui.adapters.NoteItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.master_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class MasterActivity : AppCompatActivity(), DIAware {

    override val di: DI by closestDI()
    private val viewModel by instance<MasterBookViewModel>()

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.master_activity)
        mainRecyclerView.adapter = adapter
        goToBook(MASTER_BOOK_ID)
    }

    private fun goToBook(id : String, scope : CoroutineScope = ioScope){
        viewModel.getBookById(MASTER_BOOK_ID).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> setBookView(event.value)
                is Failure -> handleError(event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun goToNote(id : String, scope : CoroutineScope = ioScope){
        viewModel.getNoteById(MASTER_BOOK_ID).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> setNoteView(event.value)
                is Failure -> handleError(event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun save(item : Any, scope : CoroutineScope = ioScope){
        viewModel.save(item).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> { print("Saved successfully") }
                is Failure -> handleError(event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun delete(id : String, scope : CoroutineScope = ioScope){
        viewModel.deleteById(id).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> { print("Deleted successfully") }
                is Failure -> handleError(event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun setBookView(book : Book, scope : CoroutineScope = ioScope){
        val isMasterBook = book.id == MASTER_BOOK_ID
        optionsLayout.isGone = isMasterBook

        bookView.isVisible = true
        noteView.isGone = true

        if(!isMasterBook){
            backButton.setOnClickListener { goToBook(MASTER_BOOK_ID) }
            deleteButton.setOnClickListener {
                delete(book.id)
                goToBook(MASTER_BOOK_ID)
            }
        }

        titleTV.text = book.title
        setBookAdapter(book.noteIds)
    }

    private fun setNoteView(note: Note){
        optionsLayout.isVisible = true

        bookView.isGone = true
        noteView.isVisible = true

        backButton.setOnClickListener { goToBook(note.ownerId) }
        deleteButton.setOnClickListener {
            delete(note.id)
            goToBook(note.ownerId)
        }

        titleTV.text = note.title
        textInput.setText(note.content)
        doneButton.setOnClickListener {
            save(note)
            goToBook(MASTER_BOOK_ID)
        }
    }

    private fun setBookAdapter(idList : List<String>){
        viewModel.getItemsFromIds(idList).collectIn(ioScope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> {
                    val result = event.value
                    result.forEach {
                        when(it){
                            is Book -> adapter.add(BookItem(it){ setBookView(it)})
                            is Note -> adapter.add(NoteItem(it, ::setNoteView))
                            else -> throw MyNotesErrors.NonNoteOrBookArgument
                        }
                    }
                }
                is Failure -> handleError(event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun setLoading(isLoading : Boolean){

    }

    private fun handleError(e : Throwable){

    }
}