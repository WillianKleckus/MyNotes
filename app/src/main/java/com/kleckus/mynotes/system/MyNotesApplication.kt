package com.kleckus.mynotes.system

import android.app.Application
import com.kleckus.mynotes.database.Database

open class MyNotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Database.initializeDatabase(this)
    }
}