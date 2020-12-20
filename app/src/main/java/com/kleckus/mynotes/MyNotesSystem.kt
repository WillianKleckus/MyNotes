package com.kleckus.mynotes

import android.app.Application
import com.kleckus.mynotes.data.di.DataModule
import com.kleckus.mynotes.database.Database
import com.kleckus.mynotes.paper_database.di.PaperModule
import com.kleckus.mynotes.system.*
import com.kleckus.mynotes.system.Util.Companion.log
import org.kodein.di.DI
import org.kodein.di.DIAware

class MyNotesSystem : Application(), DIAware{
    override fun onCreate() {
        super.onCreate()
        log("Initializing database")
        Database.initializeDatabase(applicationContext)
    }
    override val di = DI.lazy {
        import(PaperModule())
        import(DataModule())
    }

    companion object{
        fun initSystem() : Promise<Boolean> {
            val ret = Promise<Boolean>()
            Database.loadState().onComplete { returnedPair ->
                masterBook = returnedPair.second
                ret.complete(returnedPair.first)
            }
            return ret
        }

        private lateinit var masterBook : MasterBook

        fun accessMasterBook() : MasterBook {
            return if(this::masterBook.isInitialized) masterBook
            else {
                log("Master book not initialized, returning a bad Master Book")
                BAD_MASTER_BOOK
            }
        }

        fun getItemById(itemId : Int) : Any{
            if(itemId == MASTER_BOOK_ID) {
                return accessMasterBook()
            }
            else{
                accessMasterBook().bookList.forEach { book -> if(book.id == itemId) return book }
                accessMasterBook().noteList.forEach { note -> if(note.id == itemId) return note }
                accessMasterBook().bookList.forEach { book -> book.noteList.forEach { note -> if(note.id == itemId) return note } }
            }
            log("Id not found, returning a bad note")
            return BAD_NOTE
        }

        fun toggleLock(itemId : Int, password : Int) : Promise<Boolean> {
            val item : Any = getItemById(
                itemId
            ) as Lockable
            val ret = Promise<Boolean>()
            if(item is Lockable){
                if(item.isLocked && (password == item.password)) {
                    item.isLocked = false
                    item.password = NO_PASSWORD
                }
                else{
                    item.isLocked = true
                    item.password = password
                }
                Database.saveState().onComplete { success ->
                    log("Password toggled")
                    ret.complete(success)
                }
            }
            return ret
        }

        // Pair<Int, Boolean> => Pair(upperPlaceId, success)
        fun deleteItem(itemId: Int) : Promise<Pair<Int, Boolean>> {
            val ret = Promise<Pair<Int, Boolean>>()

            when(val item =
                getItemById(itemId)){
                is Book -> {
                    accessMasterBook().bookList.remove(item)
                    Database.saveState().onComplete { success -> ret.complete(Pair(
                        MASTER_BOOK_ID, success)) }
                }
                is Note -> {
                    val owner = getItemById(
                        item.ownerId
                    ) as Book
                    owner.noteList.remove(item)
                    Database.saveState().onComplete { success -> ret.complete(Pair(owner.id , success)) }
                }
                else -> {
                    ret.complete(Pair(MASTER_BOOK_ID, false))
                    log("Something went wrong")
                }
            }

            return ret
        }

        fun <NoteOrBook> createNoteOrBook(product : NoteOrBook) : Promise<Boolean> {
            val ret = Promise<Boolean>()
            when (product) {
                is Note -> {
                    accessMasterBook().highestId++

                    if(product.ownerId == masterBook.id) masterBook.noteList.add(product)
                    else { (getItemById(
                        product.ownerId
                    ) as Book).noteList.add(product) }

                    Database.saveState().onComplete { success ->
                        if(success) log("Finished creating note successfully")
                        ret.complete(success)
                    }
                }
                is Book -> {
                    accessMasterBook().highestId++
                    accessMasterBook().bookList.add(product)
                    Database.saveState().onComplete { success ->
                        if(success) log("Finished creating book successfully")
                        ret.complete(success)
                    }
                }
                else -> {
                    val success = false
                    ret.complete(success)
                    log("Something went wrong : INVALID-TYPE")
                }
            }
            return ret
        }
    }
}