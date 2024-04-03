package com.example.moviesapp.data

import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class MovieRepository {
    private val dummyMovie = mutableListOf<Movie>()

    init {
        if (dummyMovie.isEmpty()) {
            MovieData.dummyMovies.forEach {
                dummyMovie.add(it)
            }
        }
    }

    fun getMovieById(movieId: Int): Movie {
        return dummyMovie.first {
            it.id == movieId
        }
    }

    fun getFavoriteMovie(): Flow<List<Movie>> {
        return flowOf(dummyMovie.filter { it.isFavorite })
    }

    fun searchMovie(query: String) = flow {
        val data = dummyMovie.filter {
            it.title.contains(query, ignoreCase = true)
        }
        emit(data)
    }

    fun updateMovie(movieId: Int, newState: Boolean): Flow<Boolean> {
        val index = dummyMovie.indexOfFirst { it.id == movieId }
        val result = if (index >= 0) {
            val movie = dummyMovie[index]
            dummyMovie[index] = movie.copy(isFavorite = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: MovieRepository? = null

        fun getInstance(): MovieRepository =
            instance ?: synchronized(this) {
                MovieRepository().apply {
                    instance = this
                }
            }
    }
}