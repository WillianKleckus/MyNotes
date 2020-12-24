package com.kleckus.mynotes.data.di

import com.kleckus.mynotes.data.StorageImplementation
import com.kleckus.mynotes.data.cache.CacheHandler
import com.kleckus.mynotes.data.cloud.CloudHandler
import com.kleckus.mynotes.domain.services.Storage
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object DataModule{
    operator fun invoke() = DI.Module("data-module"){
        bind<CloudHandler>() with singleton {
            CloudHandler()
        }

        bind<CacheHandler>() with singleton {
            CacheHandler(database = instance(), logger = instance())
        }

        bind<Storage>() with singleton {
            StorageImplementation(cache = instance(),cloud =  instance())
        }
    }
}