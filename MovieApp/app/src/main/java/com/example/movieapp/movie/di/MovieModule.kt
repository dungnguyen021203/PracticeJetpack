package com.example.movieapp.movie.di

import com.example.movieapp.movie.common.data.ApiMapper
import com.example.movieapp.movie.data.mapper_impl.MovieApiMapperImpl
import com.example.movieapp.movie.data.remote.api.MovieApiService
import com.example.movieapp.movie.data.remote.models.MovieDto
import com.example.movieapp.movie.data.repository_impl.MovieRepositoryImpl
import com.example.movieapp.movie.domain.models.Movie
import com.example.movieapp.movie.domain.repository.MovieRepository
import com.example.movieapp.utils.K
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApiService: MovieApiService,
        mapper: ApiMapper<List<Movie>, MovieDto>
    ): MovieRepository = MovieRepositoryImpl (movieApiService, mapper)

    @Provides
    @Singleton
    fun provideMovieMapper(): ApiMapper<List<Movie>, MovieDto> = MovieApiMapperImpl()

    @Provides
    @Singleton
    fun provideMovieApiService(): MovieApiService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(MovieApiService::class.java)
    }
}