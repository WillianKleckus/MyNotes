package com.kleckus.mynotes.domain.models

import com.kleckus.mynotes.domain.Constants
import com.kleckus.mynotes.domain.models.ModularItem.Text

sealed class Item(
    val id : String,
    val ownerId : String = Constants.MASTER_BOOK_ID,
    var isLocked : Boolean,
    var password : String?
) {

    fun toggleLock(newPassword: String? = null){
        password = newPassword
        isLocked = !newPassword.isNullOrBlank()
    }

    class Book(
        id: String,
        ownerId: String,
        isLocked: Boolean,
        password: String,
        var title : String,
        var noteIds : List<String>
    ) : Item(id,ownerId,isLocked,password) {
        fun numberOfNotes() : Int = noteIds.size
    }

    class Note(
        id: String,
        ownerId: String,
        isLocked: Boolean,
        password: String,
        val items : List<ModularItem>
    ) : Item(id,ownerId,isLocked,password){
        fun numberOfNotes() =
            items.filterIsInstance<Text>().size

        fun numberOfCheckLists() =
            items.filterIsInstance<CheckListItem>()
    }
}