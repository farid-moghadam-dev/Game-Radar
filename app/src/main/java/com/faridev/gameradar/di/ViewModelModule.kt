package com.faridev.gameradar.di

import com.faridev.gameradar.presentation.feature.detail.GameDetailViewModel
import com.faridev.gameradar.presentation.feature.entity.detail.EntityDetailViewModel
import com.faridev.gameradar.presentation.feature.entity.list.EntityListViewModel
import com.faridev.gameradar.presentation.feature.home.HomeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { HomeViewModel(get()) }
    factory { GameDetailViewModel(get()) }
    factory { EntityListViewModel(get()) }
    factory { EntityDetailViewModel(get(), get()) }
}
