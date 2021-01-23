package com.kleckus.mynotes.data.cache

import com.kleckus.mynotes.data.cache.CacheHandler.Operation.ADD
import com.kleckus.mynotes.data.cache.CacheHandler.Operation.REMOVE
import com.kleckus.mynotes.domain.Constants.INITIAL_BOOK
import com.kleckus.mynotes.domain.Constants.MASTER_BOOK_ID
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Item
import com.kleckus.mynotes.domain.models.Item.Book
import com.kleckus.mynotes.domain.models.Item.Note
import com.kleckus.mynotes.domain.services.Logger
import com.kleckus.mynotes.domain.services.PaperDatabase

class CacheHandler(
    private val database : PaperDatabase,
    private val logger : Logger
){
    suspend fun save(item : Item){
        when(item){
            is Book -> {
                alter(MASTER_BOOK_ID, item.id, ADD)
                database.save(item.id, item)
            }
            is Note -> {
                alter(item.ownerId, item.id,  ADD)
                database.save(item.id, item)
            }
            else -> throw MyNotesErrors.InvalidArgumentType
        }
    }

    suspend fun save(id : Int){
        database.save(id)
    }

    suspend fun load(id : String) : Item = database.load(id, INITIAL_BOOK)

    suspend fun load() : Int = database.load()

    suspend fun delete(id: String){
        when(val item = load(id)){
            is Book -> alter(MASTER_BOOK_ID, id, REMOVE)
            is Note -> alter(item.ownerId, id,  REMOVE)
            else -> throw MyNotesErrors.InvalidArgumentType
        }
        database.delete(id)
    }

    private suspend fun alter(ownerId : String, id : String, operationType : Operation){
        val owner = load(ownerId) as Book
        val list = mutableListOf<String>()
        list.addAll(owner.noteIds)
        when(operationType){
            ADD -> if(!list.contains(id)) list.add(id)
            REMOVE -> list.remove(id)
        }
        owner.noteIds = list
        database.save(owner.id, owner)
    }

    enum class Operation{
        ADD, REMOVE
    }
}