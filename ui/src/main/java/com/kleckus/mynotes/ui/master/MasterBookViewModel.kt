package com.kleckus.mynotes.ui.master

import androidx.lifecycle.ViewModel
import cafe.adriel.dalek.Dalek
import cafe.adriel.dalek.DalekEvent
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.domain.services.Storage
import kotlinx.coroutines.flow.Flow

class MasterBookViewModel(private val service : Storage) : ViewModel() {

    fun getItemsFromIds(idList : List<String>) : Flow<DalekEvent<List<Any>>> =
        Dalek{ getItemListById(idList) }

    fun save(item : Any) : Flow<DalekEvent<Unit>> =
        Dalek{ service.save(item) }

    fun getBookById(id : String) : Flow<DalekEvent<Book>> =
        Dalek{ service.loadBook(id) }

    fun getNoteById(id : String) : Flow<DalekEvent<Note>> =
        Dalek{ service.loadNote(id) }

    fun deleteById(id : String) : Flow<DalekEvent<Unit>> =
        Dalek{ service.delete(id) }

    private suspend fun getItemListById(idList : List<String>) : List<Any> =
        idList.map {
            service.load(it)
        }

}