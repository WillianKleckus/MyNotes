package com.kleckus.mynotes.domain.services

import com.kleckus.mynotes.domain.models.ModularItem

interface Storage {
    suspend fun generateId() : String

    suspend fun save(item : ModularItem)
    suspend fun delete(id : String)
    suspend fun load(id: String) : ModularItem

    suspend fun checkForUpdates()
}