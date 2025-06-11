package com.example.movieapp.movie_detail.domain.repository

import com.example.movieapp.movie.domain.models.Movie
import com.example.movieapp.movie_detail.domain.models.MovieDetail
import com.example.movieapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    fun fetchMovieDetail(movieId: Int): Flow<Response<MovieDetail>>
    fun fetchMovie(): Flow<Response<List<Movie>>>
}