package com.padcmyanmar.smtz.themovieapp.data.models

import androidx.lifecycle.LiveData
import com.padcmyanmar.smtz.themovieapp.data.vos.*
import com.padcmyanmar.smtz.themovieapp.network.TheMovieApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

object MovieModelImpl : MovieModel, BaseModel() {

    override fun getNowPlayingMovies(
//        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>? {

        //Network
        mTheMovieApi.getNowPlayingMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                it.results?.forEach { movie -> movie.type = NOW_PLAYING }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())
            },
            {
                onFailure(it.localizedMessage ?: "")                              // { } error event
            })
        return mMovieDatabase?.movieDao()?.getMoviesByType(type = NOW_PLAYING)
    }

    override fun getPopularMovies(
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>? {

        mTheMovieApi.getPopularMovies(page=1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.results?.forEach { movie -> movie.type = POPULAR }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())
//                onSuccess(it.results ?: listOf())
            },{
                onFailure(it.localizedMessage ?: "")
            })
        return mMovieDatabase?.movieDao()?.getMoviesByType(type = POPULAR)
    }

    override fun getTopRatedMovies(
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>? {

        mTheMovieApi.getTopRatedMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.results?.forEach { movie -> movie.type = TOP_RATED }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())
//                onSuccess(it.results ?: listOf())
            },{
                onFailure(it.localizedMessage ?: "")
            })
        return mMovieDatabase?.movieDao()?.getMoviesByType(type = TOP_RATED)
    }

    override fun getGenres(onSuccess: (List<GenreVO>) -> Unit, onFailure: (String) -> Unit) {

        mTheMovieApi.getGenres()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it.genres ?: listOf())
            },{
                onFailure(it.localizedMessage ?: "")
            })
    }

    override fun getMoviesByGenre(
        genreId: String,
        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {

        mTheMovieApi.getMoviesByGenre(genreId = genreId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it.results ?: listOf())
            },{
                onFailure(it.localizedMessage ?: "")
            })
    }

    override fun getActors(onSuccess: (List<ActorVO>) -> Unit, onFailure: (String) -> Unit) {

        mTheMovieApi.getActors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it.results ?: listOf())
            },{
                onFailure(it.localizedMessage ?: "")
            })
    }

    override fun getMovieDetails(
        movieId: String,
//        onSuccess: (MovieVO) -> Unit,
        onFailure: (String) -> Unit
    ): LiveData<MovieVO?>? {

        //Network
        mTheMovieApi.getMovieDetails(movieId = movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val movieFromDatabaseToSync = mMovieDatabase?.movieDao()?.getMovieByIdOneTime(movieId.toInt())
                it.type = movieFromDatabaseToSync?.type

                mMovieDatabase?.movieDao()?.insertSingleMovie(it)
            },{
                onFailure(it.localizedMessage ?: "")
            })
        return mMovieDatabase?.movieDao()?.getMovieById(movieId.toInt())
    }

    override fun getCreditsByMovies(
        movieId: String,
        onSuccess: (Pair<List<ActorVO>, List<ActorVO>>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mTheMovieApi.getCreditsByMovies(movieId = movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(Pair(it.cast ?: listOf(), it.crew ?: listOf()))
            },{
                onFailure(it.localizedMessage ?: "")
            })
    }

    override fun searchMovies(
        query: String
    ) : Observable<List<MovieVO>>{
        return mTheMovieApi
            .searchMovies(query = query)
            .map { it.results ?: listOf() }
            .onErrorResumeNext { Observable.just(listOf()) }
            .subscribeOn(Schedulers.io())
    }

    //TheMovieApi = Observable , TheMovieModel = LiveData,Flowable -> Observable

    override fun getNowPlayingMoviesObservable(): Observable<List<MovieVO>>? {
        mTheMovieApi.getNowPlayingMovies(page = 1)                                // Observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it.results?.forEach { movie -> movie.type = NOW_PLAYING }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())
            }
        return mMovieDatabase?.movieDao()?.getMoviesByTypeFlowable(type = NOW_PLAYING)    //Flowable
            ?.toObservable()                                                              //Observable
    }

    override fun getPopularMoviesObservable(): Observable<List<MovieVO>>? {
        mTheMovieApi.getPopularMovies(page=1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it.results?.forEach { movie -> movie.type = POPULAR }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())
            }
        return mMovieDatabase?.movieDao()?.getMoviesByTypeFlowable(type = POPULAR)
            ?.toObservable()
    }

    override fun getTopRatedMoviesObservable(): Observable<List<MovieVO>>? {
        mTheMovieApi.getTopRatedMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it.results?.forEach { movie -> movie.type = TOP_RATED }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())
//                onSuccess(it.results ?: listOf())
            }
        return mMovieDatabase?.movieDao()?.getMoviesByTypeFlowable(type = TOP_RATED)
            ?.toObservable()
    }

    override fun getGenresObservable(): Observable<List<GenreVO>>? {
        return mTheMovieApi.getGenres()
            .map { it.genres ?: listOf() }
            .subscribeOn(Schedulers.io())
    }

    override fun getMoviesByGenreObservable(genreId: String): Observable<List<MovieVO>>? {
        return mTheMovieApi.getMoviesByGenre(genreId = genreId)
            .map { it.results ?: listOf() }
            .subscribeOn(Schedulers.io())
    }

    override fun getActorsObservable(): Observable<List<ActorVO>>? {
        return mTheMovieApi.getActors()
            .map { it.results ?: listOf() }
            .subscribeOn(Schedulers.io())
    }

    override fun getMovieByIdObservable(movieId: String): Observable<MovieVO?>? {
        mTheMovieApi.getMovieDetails(movieId = movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val movieFromDatabaseToSync =
                    mMovieDatabase?.movieDao()?.getMovieByIdOneTime(movieId.toInt())
                it.type = movieFromDatabaseToSync?.type

                mMovieDatabase?.movieDao()?.insertSingleMovie(it)
            }
        return mMovieDatabase?.movieDao()?.getMovieByIdFlowable(movieId.toInt())
            ?.toObservable()
    }

    override fun getCreditsByMovieObservable(movieId: String): Observable<Pair<List<ActorVO>, List<ActorVO>>> {
        return mTheMovieApi.getCreditsByMovies(movieId)
            .map { Pair(it.cast ?: listOf(), it.crew ?: listOf()) }
            .subscribeOn(Schedulers.io())
    }
}