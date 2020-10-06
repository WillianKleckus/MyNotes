package com.kleckus.mynotes.system

class Note(val id : Int, val ownerId : Int, var isLocked : Boolean, var password : String = "", var title : String, var content : String)

open class Book(val id : Int, var isLocked : Boolean, var password : String = "", var title : String, var noteList : MutableList<Note>){
    fun numberOfNotes() : Int = noteList.size
}

class MasterBook(var bookList : MutableList<Book>, var highestId : Int) : Book(MASTER_BOOK_ID, false, "", MASTER_BOOK_TITLE, mutableListOf())

const val MASTER_BOOK_ID = -1
private const val BAD_ID = -2
private const val MASTER_BOOK_TITLE = "Master Book"

val BAD_NOTE = Note(BAD_ID,BAD_ID,false,"","Bad Note", "This is a note that appears when something wrong occurs")
val BAD_BOOK = Book(BAD_ID,false,"","Bad Note", mutableListOf())
val BAD_MASTER_BOOK = MasterBook(mutableListOf(), BAD_ID)