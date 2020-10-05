package com.kleckus.mynotes.database

import android.content.Context
import com.kleckus.mynotes.system.*
import io.paperdb.Paper
import java.lang.Exception

enum class DB_KEYS(val key : String){
    NOTE("note-key"),
    BOOK("book-key")
}
enum class BOOKS(val key : String){
    MAIN_BOOK("main-book-key")
}

class Database{
    companion object{
        fun initializeDatabase(context: Context){ Paper.init(context) }

        fun loadState() : Promise<Boolean>{
            val ret = Promise<Boolean>()
            var success = false
            Async{
                try {
                    MyNotesSystem.noteList = Paper.book(BOOKS.MAIN_BOOK.key).read<MutableList<Note>>(DB_KEYS.NOTE.key)
                    MyNotesSystem.bookList = Paper.book(BOOKS.MAIN_BOOK.key).read<MutableList<Book>>(DB_KEYS.BOOK.key)
                    success = true
                }
                catch (e : Exception) { success = false }
            }.andThen { ret.complete(success) }
            return ret
        }

        fun saveState() : Promise<Boolean>{
            val ret = Promise<Boolean>()
            var success = false
            Async{
                try {
                    Paper.book(BOOKS.MAIN_BOOK.key).write(DB_KEYS.NOTE.key, MyNotesSystem.noteList)
                    Paper.book(BOOKS.MAIN_BOOK.key).write(DB_KEYS.BOOK.key, MyNotesSystem.bookList)
                    success = true
                }
                catch (e : Exception){ success = false }
            }.andThen { ret.complete(success) }
            return ret
        }
    }
}