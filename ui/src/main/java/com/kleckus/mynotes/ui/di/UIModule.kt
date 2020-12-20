package com.kleckus.mynotes.ui.di

import com.kleckus.mynotes.ui.master.MasterBookViewModel
import org.kodein.di.DI
import org.kodein.di.bind

object UIModule {
    operator fun invoke() = DI.Module("ui-module"){
        bind<MasterBookViewModel>()
    }
}