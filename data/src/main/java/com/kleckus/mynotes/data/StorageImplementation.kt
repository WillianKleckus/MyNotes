package com.kleckus.mynotes.data

import com.kleckus.mynotes.data.cache.CacheHandler
import com.kleckus.mynotes.data.cache.CacheHandler.Companion.ID_FIELD
import com.kleckus.mynotes.data.cache.CacheHandler.ItemType.ID
import com.kleckus.mynotes.data.cache.CacheHandler.ItemType.ITEM
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

    override suspend fun generateId(): String {
        val id = (cache.load(ID_FIELD, ID) as Int) + 1
        cache.save(id, ID)
        return "item-$id"
    }

    override suspend fun save(item: Any) {
        cache.save(item, ITEM)
    }

    override suspend fun delete(id: String) {
        cache.delete(id)
    }

    override suspend fun load(id: String): Any =
        cache.load(id)

}