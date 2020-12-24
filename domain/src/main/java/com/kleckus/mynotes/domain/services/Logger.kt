package com.kleckus.mynotes.domain.services

interface Logger {
    fun log(message : String)
    fun log(message : String, error : Throwable)
}