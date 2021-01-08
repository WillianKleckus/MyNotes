package com.kleckus.mynotes.domain.models

class Note(
    val id : String,
    val ownerId : String,
    var isLocked : Boolean,
    val title : String,
    var password : String? = null,
    val items : List<Item>
) {
    fun toggleLock(newPassword: String? = null){
        password = newPassword
        isLocked = !newPassword.isNullOrBlank()
    }
}