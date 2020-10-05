package com.kleckus.mynotes.database

import android.content.Context
import com.kleckus.mynotes.system.*
import com.kleckus.mynotes.system.Util.Companion.log
import io.paperdb.Paper
import java.lang.Exception

enum class DatabaseKeys(val key : String){
    NOTE("note-key"),
    NOTE_ID("note-id-key"),
    BOOK("book-key"),
    BOOK_ID("book-id-key")
}
enum class TopLevelBooks(val key : String){
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
                    MyNotesSystem.noteList = Paper.book(TopLevelBooks.MAIN_BOOK.key).read<MutableList<Note>>(DatabaseKeys.NOTE.key)
                    MyNotesSystem.lastNoteId = Paper.book(TopLevelBooks.MAIN_BOOK.key).read<Int>(DatabaseKeys.NOTE_ID.key)
                    MyNotesSystem.bookList = Paper.book(TopLevelBooks.MAIN_BOOK.key).read<MutableList<Book>>(DatabaseKeys.BOOK.key)
                    MyNotesSystem.lastBookId = Paper.book(TopLevelBooks.MAIN_BOOK.key).read<Int>(DatabaseKeys.BOOK_ID.key)
                    success = true
                }
                catch (e : Exception) {
                    success = false
                    log("Could not load previous state: ${e.message.toString()}")
                }
            }.andThen { ret.complete(success) }
            return ret
        }

        fun saveState() : Promise<Boolean>{
            val ret = Promise<Boolean>()
            var success = false
            Async{
                try {
                    Paper.book(TopLevelBooks.MAIN_BOOK.key).write(DatabaseKeys.NOTE.key, MyNotesSystem.noteList)
                    Paper.book(TopLevelBooks.MAIN_BOOK.key).write(DatabaseKeys.NOTE_ID.key, MyNotesSystem.lastNoteId)
                    Paper.book(TopLevelBooks.MAIN_BOOK.key).write(DatabaseKeys.BOOK.key, MyNotesSystem.bookList)
                    Paper.book(TopLevelBooks.MAIN_BOOK.key).write(DatabaseKeys.BOOK_ID.key, MyNotesSystem.lastBookId)
                    success = true
                }
                catch (e : Exception){
                    success = false
                    log("Could not save current state: ${e.message.toString()}")
                }
            }.andThen { ret.complete(success) }
            return ret
        }
    }
}