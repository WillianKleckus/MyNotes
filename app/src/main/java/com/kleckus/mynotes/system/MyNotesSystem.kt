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

        var lastNoteId = 0
        var noteList = mutableListOf<Note>()
        var lastBookId = 0
        var bookList = mutableListOf<Book>()

        fun <NoteOrBook> createNoteOrBook(product : NoteOrBook) : Promise<Boolean> {
            val ret = Promise<Boolean>()
            when (product) {
                is Note -> {
                    lastNoteId++
                    noteList.add(product)
                    Database.saveState().onComplete { success ->
                        if(success) log("Finished creating note successfully")
                        ret.complete(success)
                    }
                }
                is Book -> {
                    lastBookId++
                    bookList.add(product)
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