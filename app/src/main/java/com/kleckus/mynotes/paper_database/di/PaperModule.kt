package com.kleckus.mynotes.paper_database.di

import com.kleckus.mynotes.domain.services.PaperDatabase
import com.kleckus.mynotes.paper_database.PaperImplementation
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object PaperModule {
    operator fun invoke() = DI.Module("paper-database-module"){
        bind<PaperDatabase>() with singleton {
            PaperImplementation()
        }
    }
}