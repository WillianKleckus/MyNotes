package com.kleckus.mynotes.data

import com.kleckus.mynotes.data.cache.CacheHandler
import com.kleckus.mynotes.data.cloud.CloudHandler
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.domain.services.Storage

class StorageImplementation(
    private val cache : CacheHandler,
    private val cloud : CloudHandler
) : Storage {

    override suspend fun checkForUpdates(){
    }

    override suspend fun save(item: Any) {
        cache.save(item)
    }



    override suspend fun delete(id: String) {
        cache.delete(id)
    }

    override suspend fun load(id: String): Any =
        cache.load(id)

    override suspend fun loadBook(id: String): Book =
        cache.loadBook(id)

    override suspend fun loadNote(id: String): Note =
        cache.loadNote(id)
}