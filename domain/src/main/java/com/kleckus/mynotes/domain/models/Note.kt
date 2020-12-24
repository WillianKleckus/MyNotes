package com.kleckus.mynotes.domain.models

class Note(
    id : String,
    ownerId : String,
    isLocked : Boolean,
    password : String,
    var title : String,
    var content : String
) : Item(id, ownerId, isLocked, password){
    fun numberOfWords() : Int = content.trim().split(' ', '\n').size
}