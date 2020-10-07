package com.kleckus.mynotes.system

class Note(val id : Int, val ownerId : Int, isLocked : Boolean, password : Int, var title : String, var content : String) : Lockable(isLocked, password)

open class Book(val id : Int, isLocked : Boolean, password : Int, var title : String, var noteList : MutableList<Note>) : Lockable(isLocked, password){
    fun numberOfNotes() : Int = noteList.size
}

open class Lockable(var isLocked : Boolean, var password : Int)

class MasterBook(var bookList : MutableList<Book>, var highestId : Int) : Book(MASTER_BOOK_ID, false, NO_PASSWORD, MASTER_BOOK_TITLE, mutableListOf())

const val MASTER_BOOK_ID = -1
const val NO_PASSWORD = -1
const val BAD_ID = -2

private const val MASTER_BOOK_TITLE = "Master Book"

val BAD_NOTE = Note(BAD_ID,BAD_ID,false, NO_PASSWORD,"Bad Note", "This is a note that appears when something wrong occurs")
val BAD_BOOK = Book(BAD_ID,false, NO_PASSWORD,"Bad Note", mutableListOf())
val BAD_MASTER_BOOK = MasterBook(mutableListOf(), BAD_ID)