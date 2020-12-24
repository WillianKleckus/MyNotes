package com.kleckus.mynotes.logger.di

import com.kleckus.mynotes.BuildConfig
import com.kleckus.mynotes.domain.services.Logger
import com.kleckus.mynotes.logger.DebugLogger
import com.kleckus.mynotes.logger.ProductionLogger
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider

object LogModule {
    operator fun invoke() = DI.Module("log-module"){
        bind<Logger>() with provider {
            if(BuildConfig.DEBUG) DebugLogger()
            else ProductionLogger()
        }
    }
}