package com.kleckus.mynotes.ui.di

import com.kleckus.mynotes.ui.master.MasterBookViewModel
import org.kodein.di.*
import org.kodein.di.android.x.AndroidLifecycleScope


object UIModule {
    operator fun invoke() = DI.Module("ui-module"){
        bind<MasterBookViewModel>() with scoped(AndroidLifecycleScope.multiItem).singleton{
            MasterBookViewModel(service = instance())
        }
    }
}