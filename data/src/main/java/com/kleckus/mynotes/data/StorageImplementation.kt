package com.kleckus.mynotes.data

import com.kleckus.mynotes.data.cache.CacheHandler
import com.kleckus.mynotes.data.cloud.CloudHandler
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Item
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.domain.services.Storage

class StorageImplementation(
    private val cache : CacheHandler,
    private val cloud : CloudHandler
) : Storage {
    override suspend fun checkForUpdates(){
    }

    override suspend fun generateId(): String {
        val id = cache.load() + 1
        cache.save(id)
        return "item-$id"
    }

    override suspend fun save(item: Item) {
        cache.save(item)
    }

    override suspend fun delete(id: String) {
        cache.delete(id)
    }

    override suspend fun load(id: String): Item =
        cache.load(id)

}