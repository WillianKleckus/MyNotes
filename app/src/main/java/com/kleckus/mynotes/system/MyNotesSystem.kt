package com.kleckus.mynotes.system

import android.app.Application
import com.kleckus.mynotes.database.Database
import com.kleckus.mynotes.system.Util.Companion.log

class MyNotesSystem : Application() {
    override fun onCreate() {
        super.onCreate()
        log("Initializing database")
        Database.initializeDatabase(applicationContext)
    }

    companion object{
        fun initSystem() : Promise<Boolean>{
            val ret = Promise<Boolean>()
            Database.loadState().onComplete { returnedPair ->
                masterBook = returnedPair.second
                ret.complete(returnedPair.first)
            }
            return ret
        }

        private lateinit var masterBook : MasterBook

        fun accessMasterBook() : MasterBook{
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

        fun <NoteOrBook> createNoteOrBook(product : NoteOrBook) : Promise<Boolean> {
            val ret = Promise<Boolean>()
            when (product) {
                is Note -> {
                    accessMasterBook().highestId++

                    if(product.ownerId == masterBook.id) masterBook.noteList.add(product)
                    else { (getItemById(product.ownerId) as Book).noteList.add(product) }

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