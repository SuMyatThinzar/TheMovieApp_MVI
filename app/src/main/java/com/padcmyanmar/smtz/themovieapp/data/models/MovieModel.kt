package com.padcmyanmar.smtz.themovieapp.data.models

import androidx.lifecycle.LiveData
import com.padcmyanmar.smtz.themovieapp.data.vos.ActorVO
import com.padcmyanmar.smtz.themovieapp.data.vos.GenreVO
import com.padcmyanmar.smtz.themovieapp.data.vos.MovieVO
import io.reactivex.rxjava3.core.Observable
import java.util.*

interface MovieModel {
    fun getNowPlayingMovies(
//        onSuccess : (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>?

    fun getPopularMovies(
//        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>?

    fun getTopRatedMovies(
//        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>?

    fun getGenres(
        onSuccess: (List<GenreVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun getMoviesByGenre(
        genreId : String,
        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun getActors(
        onSuccess: (List<ActorVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun getMovieDetails(
        movieId : String,
//        onSuccess: (MovieVO) -> Unit,
        onFailure: (String) -> Unit
    ): LiveData<MovieVO?>?

    fun getCreditsByMovies(
        movieId: String,
        onSuccess: (Pair<List<ActorVO>, List<ActorVO>>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun searchMovies(
        query: String
    ): Observable<List<MovieVO>>

    //Reactive Streams only
    fun getNowPlayingMoviesObservable(): Observable<List<MovieVO>>?
    fun getPopularMoviesObservable(): Observable<List<MovieVO>>?
    fun getTopRatedMoviesObservable(): Observable<List<MovieVO>>?
    fun getGenresObservable(): Observable<List<GenreVO>>?
    fun getMoviesByGenreObservable(genreId : String): Observable<List<MovieVO>>?
    fun getActorsObservable(): Observable<List<ActorVO>>?
    fun getMovieByIdObservable(movieId: String): Observable<MovieVO?>?
    fun getCreditsByMovieObservable(movieId: String): Observable<Pair<List<ActorVO>,List<ActorVO>>>

}