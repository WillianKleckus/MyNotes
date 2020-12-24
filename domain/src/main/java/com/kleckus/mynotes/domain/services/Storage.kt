package com.kleckus.mynotes.domain.services

import com.kleckus.mynotes.domain.models.Item

interface Storage {
    suspend fun generateId() : String

    suspend fun save(item : Item)
    suspend fun delete(id : String)
    suspend fun load(id: String) : Item

    suspend fun checkForUpdates()
}