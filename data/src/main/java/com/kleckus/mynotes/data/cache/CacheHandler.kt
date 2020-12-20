package com.kleckus.mynotes.data.cache

import com.kleckus.mynotes.data.cache.CacheHandler.ItemType.ID
import com.kleckus.mynotes.data.cache.CacheHandler.ItemType.ITEM
import com.kleckus.mynotes.data.cache.CacheHandler.Operation.ADD
import com.kleckus.mynotes.data.cache.CacheHandler.Operation.REMOVE
import com.kleckus.mynotes.domain.Constants.INITIAL_BOOK
import com.kleckus.mynotes.domain.Constants.MASTER_BOOK_ID
import com.kleckus.mynotes.domain.MyNotesErrors
import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Note
import com.kleckus.mynotes.domain.services.Logger
import com.kleckus.mynotes.domain.services.PaperDatabase

class CacheHandler(
    private val database : PaperDatabase,
    private val logger : Logger
){
    companion object{
        const val ID_FIELD = "id-field"
    }

    suspend fun save(item : Any, saveType: ItemType){
        when(saveType){
            ID -> database.save(ID_FIELD, item as Int)
            ITEM -> {
                when(item){
                    is Book -> {
                        alter(MASTER_BOOK_ID, item.id, ADD)
                        database.save(item.id, item)
                    }
                    is Note -> {
                        alter(item.ownerId, item.id,  ADD)
                        database.save(item.id, item)
                    }
                    else -> throw MyNotesErrors.NonNoteOrBookArgument
                }
            }
        }
    }

    suspend fun load(id : String, loadType : ItemType = ITEM) : Any =
        when(loadType){
            ID -> database.load(id, 0)
            ITEM -> database.load(id, INITIAL_BOOK)
        }

    suspend fun delete(id: String){
        when(val item = load(id)){
            is Book -> alter(MASTER_BOOK_ID, id, REMOVE)
            is Note -> alter(item.ownerId, id,  REMOVE)
            else -> throw MyNotesErrors.NonNoteOrBookArgument
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

    enum class ItemType{
        ID, ITEM
    }
}