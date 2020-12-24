package com.kleckus.mynotes.domain.models

import com.kleckus.mynotes.domain.Constants.MASTER_BOOK_ID

class Book(
    id : String,
    ownerId : String = MASTER_BOOK_ID,
    isLocked : Boolean,
    password : String,
    var title : String,
    var noteIds : List<String>
) : Item(id, ownerId, isLocked, password){
    fun numberOfNotes() : Int = noteIds.size
}