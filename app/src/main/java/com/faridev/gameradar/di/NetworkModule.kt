package com.faridev.gameradar.di

import com.faridev.gameradar.data.remote.GameApi
import com.faridev.gameradar.data.remote.KtorClientFactory
import org.koin.dsl.module

val networkModule = module {
    single { KtorClientFactory.create() }
    single { GameApi(get()) }
}