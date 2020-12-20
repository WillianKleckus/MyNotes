package com.kleckus.mynotes.domain.services

interface PaperDatabase {
    suspend fun save(key : String, item : Any)
    suspend fun load(key: String, default : Any) : Any
    suspend fun delete(key: String)
}