package com.kleckus.mynotes.domain.models

class Book(val id : String, var isLocked : Boolean, var password : String, var title : String, var noteIds : List<String>){
    fun numberOfNotes() : Int = noteIds.size

    fun toggleLock(newPassword: String?){
        newPassword?.let {
            isLocked = true
            password = newPassword
        } ?: run{
            isLocked = false
            password = ""
        }
    }
}