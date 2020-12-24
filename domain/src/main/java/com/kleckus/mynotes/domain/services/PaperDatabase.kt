package com.kleckus.mynotes.domain.services

import com.kleckus.mynotes.domain.models.Item

interface PaperDatabase {
    suspend fun save(key : String, item : Item)
    suspend fun save(maxId : Int)

    suspend fun load(key: String, default : Item) : Item
    suspend fun load() : Int

    suspend fun delete(key: String)
}