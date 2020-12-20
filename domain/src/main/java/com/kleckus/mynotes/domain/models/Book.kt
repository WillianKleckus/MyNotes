package com.kleckus.mynotes.domain.models

class Book(val id : String, isLocked : Boolean, password : Int, var title : String, var noteList : MutableList<Note>){
    fun numberOfNotes() : Int = noteList.size
}