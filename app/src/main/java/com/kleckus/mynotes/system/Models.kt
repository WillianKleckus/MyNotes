package com.kleckus.mynotes.system

class Note(val id : Int, val isLocked : Boolean, val password : String = "", val title : String, val content : String)

class Book(val id : Int, val isLocked : Boolean, val password : String = "", val title : String, val content : MutableList<Note>){
    fun numberOfNotes() : Int = content.size
}