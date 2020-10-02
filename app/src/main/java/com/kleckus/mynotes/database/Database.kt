package com.kleckus.mynotes.database

import android.content.Context
import io.paperdb.Paper

class Database{
    companion object{
        fun initializeDatabase(context: Context){ Paper.init(context) }
    }
}