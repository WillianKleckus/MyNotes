package com.kleckus.mynotes.domain.services

import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Note

interface Storage {
    suspend fun save(item : Any)
    suspend fun delete(id : String)
    suspend fun loadBook(id: String) : Book
    suspend fun loadNote(id: String) : Note

    suspend fun checkForUpdates()
}