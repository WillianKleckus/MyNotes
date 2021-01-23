package com.kleckus.mynotes.dialog_creator

import com.kleckus.mynotes.dialog_creator.internal.DialogCreator
import com.kleckus.mynotes.dialog_creator.internal.YesOrNoDialogÏmplementation
import com.kleckus.mynotes.dialog_creator.service.DialogService
import com.kleckus.mynotes.dialog_creator.service.YesOrNoDialog
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

object DialogModule{
    operator fun invoke() = DI.Module("dialog-service-module"){
        bind<DialogService>() with provider {
            DialogCreator()
        }

        bind<YesOrNoDialog>() with provider {
            YesOrNoDialogÏmplementation(dialogService = instance())
        }
    }
}