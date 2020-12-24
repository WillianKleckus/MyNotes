package com.kleckus.mynotes.domain.models

open class Item(val id : String, val ownerId : String, var isLocked : Boolean, var password : String? = null){
    fun toggleLock(newPassword: String? = null){
        password = newPassword
        isLocked = !newPassword.isNullOrBlank()
    }
}