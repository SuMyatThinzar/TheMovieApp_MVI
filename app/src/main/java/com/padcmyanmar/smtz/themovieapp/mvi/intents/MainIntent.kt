package com.padcmyanmar.smtz.themovieapp.mvi.intents

import com.padcmyanmar.smtz.themovieapp.mvi.mvibase.MVIIntent

sealed class MainIntent : MVIIntent {
    class LoadMoviesByGenreIntent(val genrePosition: Int) : MainIntent()
    object LoadAllHomePageData : MainIntent()
}