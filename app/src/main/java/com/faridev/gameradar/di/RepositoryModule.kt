package com.faridev.gameradar.di

import com.faridev.gameradar.data.repository.GameRepositoryImpl
import com.faridev.gameradar.domain.repository.GameRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<GameRepository> { GameRepositoryImpl(get()) }
}
