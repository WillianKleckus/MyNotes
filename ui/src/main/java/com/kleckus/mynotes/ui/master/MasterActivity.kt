package com.kleckus.mynotes.ui.master

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cafe.adriel.dalek.*
import com.kleckus.mynotes.domain.Constants.MASTER_BOOK_ID
import com.kleckus.mynotes.domain.Constants.NO_PASSWORD
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Item
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.domain.services.Logger
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.adapters.BookItem
import com.kleckus.mynotes.ui.adapters.NoteItem
import com.kleckus.mynotes.ui.dialogs.ConfirmationDialog
import com.kleckus.mynotes.ui.dialogs.CreationType
import com.kleckus.mynotes.ui.dialogs.CreationType.BOOK
import com.kleckus.mynotes.ui.dialogs.CreationType.NOTE
import com.kleckus.mynotes.ui.dialogs.NoteOrBookCreationDialog
import com.kleckus.mynotes.ui.dialogs.PasswordDialog
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

    private inline fun <reified T> load(
        id : String,
        scope : CoroutineScope = mainScope,
        crossinline andThen: (item: T) -> Unit
    ){
        viewModel.getItemById(id).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> andThen(event.value as T)
                is Failure -> handleError("Error on load()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun save(ownerId: String = MASTER_BOOK_ID, item : Item, scope : CoroutineScope = mainScope){
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
        ConfirmationDialog.openDialog(this){ confirmed ->
            if(confirmed)
                viewModel.deleteById(id).collectIn(scope){ event ->
                    when(event){
                        is Start -> setLoading(true)
                        is Success -> goTo(ownerId)
                        is Failure -> handleError("Error on deleted()", event.exception)
                        is Finish -> setLoading(false)
                    }
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

    private fun goTo(id : String){
        load<Item>(id){ item ->
            when(item){
                is Book -> setBookView(item)
                is Note -> setNoteView(item)
            }
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
                            is Book -> adapter.add(BookItem(item, ::onItemClicked, ::toggleLock))
                            is Note -> adapter.add(NoteItem(item, ::onItemClicked, ::toggleLock))
                            else -> throw MyNotesErrors.InvalidArgumentType
                        }
                    }
                }
                is Failure -> handleError("Error on getItemsFromIds()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun onItemClicked(item : Item){
        if(item.isLocked){
            PasswordDialog.openDialog(item.id, false, this){ _, password ->
                if(password == item.password) goTo(item.id)
                else showError(MyNotesErrors.InvalidPassword)
            }
        } else goTo(item.id)
    }

    private fun setBookView(book : Book){
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
        deleteButton.setOnClickListener { delete(id = note.id) }

        titleTV.text = note.title
        textInput.setText(note.content)
        doneButton.setOnClickListener {
            note.content = textInput.text.toString()
            save(note.ownerId, note)
        }
    }

    private fun toggleLock(id : String, password : String){
        load<Item>(id){ item ->
            if(item.isLocked){
                if(password == item.password){
                    item.toggleLock()
                    save(item.ownerId, item)
                }
            } else{
                item.toggleLock(password)
                save(item.ownerId, item)
            }
        }
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

    private fun showError(error : Throwable){
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

    private fun handleError(message: String, e : Throwable){
        logger.log(message, e)
    }
}