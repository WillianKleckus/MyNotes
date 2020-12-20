package com.kleckus.mynotes.domain.services

interface Storage {
    suspend fun generateId() : String

    suspend fun save(item : Any)
    suspend fun delete(id : String)
    suspend fun load(id: String) : Any

    suspend fun checkForUpdates()
}