package com.kleckus.mynotes.data.cache

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
        database.load(id)

    suspend fun delete(id: String){
        database.delete(id)
    }

    suspend fun loadBook(id: String): Book =
        database.load(id) as Book

    suspend fun loadNote(id: String): Note =
        database.load(id) as Note
}