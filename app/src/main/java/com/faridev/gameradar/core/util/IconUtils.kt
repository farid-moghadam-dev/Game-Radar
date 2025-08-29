package com.faridev.gameradar.core.util

import com.faridev.gameradar.R
import com.faridev.gameradar.domain.model.ParentPlatformInfo
import com.faridev.gameradar.domain.model.Store

fun ParentPlatformInfo.iconResId(): Int = when (slug?.lowercase()) {
    "pc" -> R.drawable.ic_windows
    "playstation" -> R.drawable.ic_play_station
    "xbox" -> R.drawable.ic_xbox
    "ios" -> R.drawable.ic_ios
    "android" -> R.drawable.ic_android
    "mac" -> R.drawable.ic_mac
    "linux" -> R.drawable.ic_linux
    "nintendo" -> R.drawable.ic_nintendo
    "atari" -> R.drawable.ic_atari
    "commodore-amiga" -> R.drawable.ic_commodore
    "sega" -> R.drawable.ic_sega
    "neo-geo", "3do" -> R.drawable.ic_3do
    "web" -> R.drawable.ic_web
    else -> R.drawable.ic_web
}

fun Store.iconResId(): Int = when (slug?.lowercase()) {
    "steam" -> R.drawable.ic_steam
    "playstation-store" -> R.drawable.ic_play_station
    "xbox360", "xbox-store" -> R.drawable.ic_xbox
    "apple-appstore" -> R.drawable.ic_app_store
    "gog" -> R.drawable.ic_gog
    "nintendo" -> R.drawable.ic_nintendo
    "google-play" -> R.drawable.ic_google_play
    "itch" -> R.drawable.ic_itch_store
    "epic-games" -> R.drawable.ic_epic_games
    else -> R.drawable.ic_web
}