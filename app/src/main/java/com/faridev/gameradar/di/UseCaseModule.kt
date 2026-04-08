package com.faridev.gameradar.di

import com.faridev.gameradar.domain.usecase.FetchEntityDetailsUseCase
import com.faridev.gameradar.domain.usecase.FetchEntityListUseCase
import com.faridev.gameradar.domain.usecase.FetchGameDetailsUseCase
import com.faridev.gameradar.domain.usecase.FetchGamesByEntityUseCase
import com.faridev.gameradar.domain.usecase.FetchGamesListUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { FetchGamesListUseCase(get()) }
    factory { FetchGameDetailsUseCase(get()) }
    factory { FetchEntityListUseCase(get()) }
    factory { FetchEntityDetailsUseCase(get()) }
    factory { FetchGamesByEntityUseCase(get()) }
}
