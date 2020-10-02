package com.kleckus.mynotes.system

import com.kleckus.mynotes.database.Database

class MyNotesSystem : MyNotesApplication() {
    companion object{
        fun initSystem() : Promise<Boolean>{
            val ret = Promise<Boolean>()
            Database.loadState().onComplete { success -> success}
            return ret
        }

        var noteList = mutableListOf<Note>()
        var bookList = mutableListOf<Book>()

        fun addNote(){
        }
    }
}