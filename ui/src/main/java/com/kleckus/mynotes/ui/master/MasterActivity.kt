package com.kleckus.mynotes.ui.master

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cafe.adriel.dalek.*
import com.kleckus.mynotes.dialog_creator.service.DialogService
import com.kleckus.mynotes.dialog_creator.service.YesOrNoDialog
import com.kleckus.mynotes.domain.Constants.MASTER_BOOK_ID
import com.kleckus.mynotes.domain.Constants.NO_PASSWORD
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Item
import com.kleckus.mynotes.domain.models.Item.*
import com.kleckus.mynotes.domain.services.Logger
import com.kleckus.mynotes.ui.R
import com.kleckus.mynotes.ui.adapters.MasterItem
import com.kleckus.mynotes.ui.dialogs.CreationType
import com.kleckus.mynotes.ui.dialogs.CreationType.BOOK
import com.kleckus.mynotes.ui.dialogs.CreationType.NOTE
import com.kleckus.mynotes.ui.dialogs.PasswordDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.add_note_or_book_dialog.view.*
import kotlinx.android.synthetic.main.add_note_or_book_dialog.view.chipGroup
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
    private val dialogService by instance<DialogService>()
    private val yesOrNoDialog by instance<YesOrNoDialog>()

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.master_activity)
        mainRecyclerView.adapter = adapter
        goTo(MASTER_BOOK_ID)
    }

    private fun load(
        id : String,
        scope : CoroutineScope = mainScope,
        andThen: (item: Item) -> Unit
    ){
        viewModel.getItemById(id).collectIn(scope){ event ->
            when(event){
                is Start -> setLoading(true)
                is Success -> andThen(event.value)
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
        yesOrNoDialog.create(this){ confirmed ->
            logger.log("item-deleted")
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
                is Failure -> handleError("Error on nextAvailableId()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun goTo(id : String){
        load(id){ item ->
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
                    event.value.forEach { item ->
                        adapter.add(MasterItem(item, dialogService, ::onItemClicked, ::toggleLock))
                    }
                }
                is Failure -> handleError("Error on setBookAdapter()", event.exception)
                is Finish -> setLoading(false)
            }
        }
    }

    private fun onItemClicked(item : Item){
        if(item.isLocked){
           PasswordDialog(
               context = this,
               dialogService = dialogService,
               id = item.id,
               isLocking = false
           ) { _, password ->
                if(password == item.password) goTo(item.id)
                else showError(MyNotesErrors.InvalidPassword)
            }
        } else goTo(item.id)
    }

    private fun setBookView(book : Book){
        setupToolbar(book)

        bookView.isVisible = true
        modularNoteView.isGone = true

        addButton.setOnClickListener { createNoteOrBook(book.id) }
        setBookAdapter(book.noteIds)
    }

    private fun setNoteView(note: Note){
        setupToolbar(note)
        optionsLayout.isVisible = true

        bookView.isGone = true
        modularNoteView.isVisible = true

        setupToolbar(note)
        modularNoteView.applyNote(note)
        modularNoteView.onDoneClicked { note -> save(note.ownerId, note) }
    }

    private fun setupToolbar(item : Item){
        optionsLayout.isGone = item.id == MASTER_BOOK_ID

        backButton.setOnClickListener { goTo(item.ownerId) }
        deleteButton.setOnClickListener { delete(id = item.id) }
        titleTV.text = item.title
    }

    private fun toggleLock(id : String, password : String){
        load(id){ item ->
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
        dialogService.create(
            context = this,
            resId = R.layout.add_note_or_book_dialog
        ){ dialog ->
            chipGroup.isGone = ownerId != MASTER_BOOK_ID

            bookChip.setOnClickListener {
                if(noteChip.isChecked) noteChip.isChecked = false
            }

            noteChip.setOnClickListener {
                if(bookChip.isChecked) bookChip.isChecked = false
            }

            doneButton.setOnClickListener {
                if(bookChip.isChecked)
                    doCreate(titleInput.text.toString(), ownerId, BOOK)
                else
                    doCreate(titleInput.text.toString(), ownerId, NOTE)
                dialog.dismiss()
            }
        }
    }

    private fun doCreate(title : String, ownerId: String, type : CreationType){
        nextAvailableId { id ->
            when (type) {
                BOOK -> {
                    val book = Book(
                        id = id,
                        ownerId = MASTER_BOOK_ID,
                        isLocked = false,
                        password = NO_PASSWORD,
                        title = title,
                        noteIds = mutableListOf()
                    )
                    save(item = book)
                    logger.log("book-created")
                }
                NOTE -> {
                    val note = Note(
                        id = id,
                        ownerId = ownerId,
                        isLocked = false,
                        password = NO_PASSWORD,
                        title = title,
                        items = listOf()
                    )
                    save(note.ownerId, note)
                    logger.log("note-created")
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