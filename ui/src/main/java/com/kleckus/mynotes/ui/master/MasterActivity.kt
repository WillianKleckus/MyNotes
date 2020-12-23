package com.kleckus.mynotes.ui.master

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cafe.adriel.dalek.*
import com.kleckus.mynotes.domain.Constants.MASTER_BOOK_ID
import com.kleckus.mynotes.domain.Constants.NO_PASSWORD
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.domain.services.Logger
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.adapters.BookItem
import com.kleckus.mynotes.ui.adapters.NoteItem
import com.kleckus.mynotes.ui.dialogs.CreationType
import com.kleckus.mynotes.ui.dialogs.CreationType.BOOK
import com.kleckus.mynotes.ui.dialogs.CreationType.NOTE
import com.kleckus.mynotes.ui.dialogs.NoteOrBookCreationDialog
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
    private val logger by instance<Logger>()

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.master_activity)
        mainRecyclerView.adapter = adapter
        goTo(MASTER_BOOK_ID)
    }

    private fun goTo(id : String, scope : CoroutineScope = mainScope){
        viewModel.getItemById(id).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> {
                    when(val item = event.value){
                        is Book -> setBookView(item)
                        is Note -> setNoteView(item)
                        else -> throw MyNotesErrors.NonNoteOrBookArgument
                    }
                }
                is Failure -> handleError("Error on goTo()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun save(ownerId: String = MASTER_BOOK_ID, item : Any, scope : CoroutineScope = mainScope){
        viewModel.save(item).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> goTo(ownerId)
                is Failure -> handleError("Error on save()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun delete(ownerId: String = MASTER_BOOK_ID, id : String, scope : CoroutineScope = mainScope){
        viewModel.deleteById(id).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> goTo(ownerId)
                is Failure -> handleError("Error on deleted()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun nextAvailableId(scope : CoroutineScope = mainScope, andThen : (id : String) -> Unit){
        viewModel.getNextAvailableId().collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> andThen(event.value)
                is Failure -> handleError("Error on deleted()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun setBookView(book : Book, scope : CoroutineScope = mainScope){
        val isMasterBook = book.id == MASTER_BOOK_ID
        optionsLayout.isGone = isMasterBook

        bookView.isVisible = true
        noteView.isGone = true

        if(!isMasterBook){
            backButton.setOnClickListener { goTo(MASTER_BOOK_ID) }
            deleteButton.setOnClickListener { delete(id = book.id) }
        } else{
            deleteButton.setOnClickListener(null)
            backButton.setOnClickListener(null)
        }

        addButton.setOnClickListener { createNoteOrBook(book.id) }
        titleTV.text = book.title
        setBookAdapter(book.noteIds)
    }

    private fun setNoteView(note: Note){
        optionsLayout.isVisible = true

        bookView.isGone = true
        noteView.isVisible = true

        backButton.setOnClickListener { goTo(note.ownerId) }
        deleteButton.setOnClickListener {
            delete(id = note.id)
            goTo(note.ownerId)
        }

        titleTV.text = note.title
        textInput.setText(note.content)
        doneButton.setOnClickListener {
            note.content = textInput.text.toString()
            save(note.ownerId, note)
        }
    }

    private fun setBookAdapter(idList : List<String>){
        adapter.clear()
        viewModel.getItemsFromIds(idList).collectIn(mainScope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> {
                    val result = event.value
                    logger.log(result.toString())
                    result.forEach { item ->
                        when(item){
                            is Book -> adapter.add(BookItem(item, {setBookView(item)} , ::toggleLock))
                            is Note -> adapter.add(NoteItem(item, ::setNoteView, ::toggleLock))
                            else -> throw MyNotesErrors.NonNoteOrBookArgument
                        }
                    }
                }
                is Failure -> handleError("Error on getItemsFromIds()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun <T> toggleLock(item : T, password : String? = null){
        val ownerId : String
        when(item){
            is Book -> item.toggleLock(password).also { ownerId = MASTER_BOOK_ID }
            is Note -> item.toggleLock(password).also { ownerId = item.ownerId }
            else -> throw MyNotesErrors.NonNoteOrBookArgument
        }
        save(ownerId, item)
    }

    private fun createNoteOrBook(ownerId : String){
        NoteOrBookCreationDialog.openDialog(ownerId, ::doCreate, this)
    }

    private fun doCreate(title : String, ownerId: String, type : CreationType){
        nextAvailableId { id ->
            when (type) {
                BOOK -> {
                    val book = Book(
                        id = id,
                        isLocked = false,
                        password = NO_PASSWORD,
                        title = title,
                        noteIds = mutableListOf()
                    )
                    save(item = book)
                }
                NOTE -> {
                    val note = Note(
                        id = id,
                        ownerId = ownerId,
                        isLocked = false,
                        password = NO_PASSWORD,
                        title = title,
                        content = ""
                    )
                    save(note.ownerId, note)
                }
            }
        }
    }

    private fun setLoading(isLoading : Boolean){
        loadingView.isGone = !isLoading
    }

    private fun handleError(message: String, e : Throwable){
        logger.log(message, e)
    }
}