package com.kleckus.mynotes.domain.models

class Book(val id : String, var isLocked : Boolean, var password : Int, var title : String, var noteIds : List<String>){
    fun numberOfNotes() : Int = noteIds.size
}