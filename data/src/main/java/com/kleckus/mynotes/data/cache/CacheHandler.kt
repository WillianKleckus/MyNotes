package com.kleckus.mynotes.data.cache

import com.kleckus.mynotes.domain.Constants
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.domain.services.PaperDatabase

class CacheHandler(private val database : PaperDatabase){
    suspend fun save(item : Any){
        when(item){
            is Book -> database.save(item.id, item)
            is Note -> database.save(item.id, item)
            else -> throw MyNotesErrors.NonNoteOrBookArgument
        }
    }

    suspend fun load(id : String) : Any =
        database.load(id, Constants.INITIAL_BOOK)

    suspend fun delete(id: String){
        database.delete(id)
    }
}