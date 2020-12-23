package com.kleckus.mynotes.domain.models

import com.kleckus.mynotes.domain.Constants.NO_PASSWORD

class Note(val id : String, val ownerId : String, var isLocked : Boolean, var password : String, var title : String, var content : String){
    fun numberOfWords() : Int = content.trim().split(' ', '\n').size

    fun toggleLock(newPassword: String?){
        newPassword?.let {
            isLocked = true
            password = newPassword
        } ?: run{
            isLocked = false
            password = NO_PASSWORD
        }
    }
}