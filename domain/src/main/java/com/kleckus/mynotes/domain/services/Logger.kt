package com.kleckus.mynotes.domain.services

interface Logger {
    fun log(message : String, isImportant : Boolean = false)
    fun log(message : String, error : Throwable)
}