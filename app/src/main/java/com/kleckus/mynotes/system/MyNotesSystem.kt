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
            Database.loadState().onComplete { success -> ret.complete(success) }
            return ret
        }

        lateinit var masterBook : MasterBook

        fun <NoteOrBook> createNoteOrBook(product : NoteOrBook) : Promise<Boolean> {
            val ret = Promise<Boolean>()
            when (product) {
                is Note -> {
                    masterBook.highestId++

                    if(product.ownerId == masterBook.id) masterBook.noteList.add(product)
                    else { masterBook.bookList.forEach { book -> if(book.id == product.ownerId) book.noteList.add(product) } }

                    Database.saveState().onComplete { success ->
                        if(success) log("Finished creating note successfully")
                        ret.complete(success)
                    }
                }
                is Book -> {
                    masterBook.highestId++
                    masterBook.bookList.add(product)
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