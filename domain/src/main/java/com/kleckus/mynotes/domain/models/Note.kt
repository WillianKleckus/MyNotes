package com.kleckus.mynotes.domain.models

class Note(val id : String, val ownerId : String, isLocked : Boolean, password : Int, var title : String, var content : String){
    fun numberOfWords() : Int = content.trim().split(' ', '\n').size
}