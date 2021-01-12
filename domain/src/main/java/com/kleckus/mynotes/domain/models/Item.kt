package com.kleckus.mynotes.domain.models

import com.kleckus.mynotes.domain.Constants
import com.kleckus.mynotes.domain.models.ModularItem.Text

sealed class Item(
    val id : String,
    val ownerId : String = Constants.MASTER_BOOK_ID,
    var isLocked : Boolean,
    var title : String,
    var password : String?
) {

    fun toggleLock(newPassword: String? = null){
        password = newPassword
        isLocked = !newPassword.isNullOrBlank()
    }

    fun getDescription() =
        when(this){
            is Book -> "${numberOfNotes()} notes"
            is Note -> "${numberOfNotes()} texts - ${numberOfCheckLists().size} check lists"
        }

    class Book(
        id: String,
        ownerId: String,
        isLocked: Boolean,
        title : String,
        password: String,
        var noteIds : List<String>
    ) : Item(id,ownerId,isLocked, title, password) {
        fun numberOfNotes() : Int = noteIds.size
    }

    class Note(
        id: String,
        ownerId: String,
        isLocked: Boolean,
        title : String,
        password: String,
        val items : List<ModularItem>
    ) : Item(id,ownerId,isLocked, title, password){
        fun numberOfNotes() =
            items.filterIsInstance<Text>().size

        fun numberOfCheckLists() =
            items.filterIsInstance<CheckListItem>()
    }
}