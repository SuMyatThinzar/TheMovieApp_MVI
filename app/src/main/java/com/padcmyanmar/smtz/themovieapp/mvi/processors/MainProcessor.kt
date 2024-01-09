package com.padcmyanmar.smtz.themovieapp.mvi.processors

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.padcmyanmar.smtz.themovieapp.data.models.MovieModel
import com.padcmyanmar.smtz.themovieapp.data.models.MovieModelImpl
import com.padcmyanmar.smtz.themovieapp.mvi.states.MainState
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

object MainProcessor {

    private val mMovieModel: MovieModel = MovieModelImpl

    fun loadAllHomePageData(previousState: MainState) : LiveData<MainState> {

        return Observable.zip(
            mMovieModel.getNowPlayingMoviesObservable(),
            mMovieModel.getPopularMoviesObservable(),
            mMovieModel.getTopRatedMoviesObservable(),
            mMovieModel.getGenresObservable(),
            mMovieModel.getActorsObservable()
        ) { nowPlaying, popular, topRated, genres, actors ->
            return@zip previousState.copy(
                isLoading = false,
                errorMessage = "",
                nowPlayingMovies = nowPlaying,
                popularMovies = popular,
                topRatedMovies = topRated,
                moviesByGenre = previousState.moviesByGenre,
                genres = genres,
                actors = actors
            )
        }.toFlowable(BackpressureStrategy.BUFFER)
            .toLiveData()
    }

    fun loadMoviesByGenre(previousState: MainState, genreId: Int) : LiveData<MainState> {

        return mMovieModel.getMoviesByGenreObservable(genreId.toString())
            ?.map { previousState.copy(
                isLoading = false,
                errorMessage = "",
                nowPlayingMovies = previousState.nowPlayingMovies,
                popularMovies = previousState.popularMovies,
                topRatedMovies = previousState.topRatedMovies,
                moviesByGenre = it,
                genres = previousState.genres,
                actors = previousState.actors
            ) }?.subscribeOn(Schedulers.io())
            ?.toFlowable(BackpressureStrategy.BUFFER)
            ?.toLiveData()!!
    }
}