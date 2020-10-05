package com.kleckus.mynotes.system

import com.kleckus.mynotes.database.Database

class MyNotesSystem : MyNotesApplication() {
    companion object{
        fun initSystem() : Promise<Boolean>{
            val ret = Promise<Boolean>()
            Database.loadState().onComplete { success -> ret.complete(success)}
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
                        ret.complete(success)
                    }
                }
                is Book -> {
                    lastBookId++
                    bookList.add(product)
                    Database.saveState().onComplete { success ->
                        ret.complete(success)
                    }
                }
                else -> {
                    val success = false
                    ret.complete(success)
                }
            }
            return ret
        }
    }
}