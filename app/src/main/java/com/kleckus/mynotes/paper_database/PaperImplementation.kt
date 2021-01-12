package com.kleckus.mynotes.paper_database

import com.kleckus.mynotes.domain.models.Item
import com.kleckus.mynotes.domain.models.ModularItem
import com.kleckus.mynotes.domain.services.PaperDatabase
import io.paperdb.Paper

class PaperImplementation : PaperDatabase {
    companion object{
        const val ID_FIELD = "id-field"
        const val DEFAULT_ID = 0
    }

    override suspend fun save(key: String, item: Item) {
        Paper.book().write(key,item)
    }

    override suspend fun save(maxId: Int) {
        Paper.book().write(ID_FIELD,maxId)
    }

    override suspend fun load(key: String, default : Item): Item =
        Paper.book().read<Item>(key, default)

    override suspend fun load(): Int =
        Paper.book().read<Int>(ID_FIELD, DEFAULT_ID)

    override suspend fun delete(key: String) {
        Paper.book().delete(key)
    }
}