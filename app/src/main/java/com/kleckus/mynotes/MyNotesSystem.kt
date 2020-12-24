package com.kleckus.mynotes

import android.app.Application
import com.kleckus.mynotes.data.di.DataModule
import com.kleckus.mynotes.logger.di.LogModule
import com.kleckus.mynotes.paper_database.di.PaperModule
import com.kleckus.mynotes.ui.di.UIModule
import io.paperdb.Paper
import org.kodein.di.DI
import org.kodein.di.DIAware

class MyNotesSystem : Application(), DIAware{
    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
    }
    override val di = DI.lazy {
        import(PaperModule())
        import(DataModule())
        import(UIModule())
        import(LogModule())
    }
}