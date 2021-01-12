package com.kleckus.mynotes.domain.services

import com.kleckus.mynotes.domain.models.ModularItem

interface PaperDatabase {
    suspend fun save(key : String, item : ModularItem)
    suspend fun save(maxId : Int)

    suspend fun load(key: String, default : ModularItem) : ModularItem
    suspend fun load() : Int

    suspend fun delete(key: String)
}