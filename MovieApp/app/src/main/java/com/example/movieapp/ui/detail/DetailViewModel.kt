package com.example.movieapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.movieapp.movie.domain.models.Movie
import com.example.movieapp.movie_detail.domain.models.MovieDetail
import com.example.movieapp.movie_detail.domain.repository.MovieDetailRepository
import com.example.movieapp.utils.K
import com.example.movieapp.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository,
    savedStateHandle: SavedStateHandle // help track the id in navigation
): ViewModel(){
    private val _detailState = MutableStateFlow(DetailState())
    val detailState = _detailState.asStateFlow()

    val id: Int = savedStateHandle.get<Int>(K.MOVIE_ID) ?: -1

    init {
        fetchMovieDetailById()
    }

    private fun fetchMovieDetailById() = viewModelScope.launch {
        if (id == -1) {
            _detailState.update {
                it.copy(isLoading = false, error = "Movie not found")
            }
        } else {
            repository.fetchMovieDetail(id).collectAndHandle(
                onError = { error ->
                    _detailState.update {
                        it.copy(isLoading = false, error = error?.message)
                    }
                },
                onLoading = {
                    _detailState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
            ) { movieDetail ->
                _detailState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        movieDetail = movieDetail
                    )
                }
            }
        }
    }

    fun fetchMovie() = viewModelScope.launch {
        repository.fetchMovie().collectAndHandle(
            onError = { error ->
                _detailState.update {
                    it.copy(isMovieLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _detailState.update {
                    it.copy(isMovieLoading = true, error = null)
                }
            }
        ) { movies ->
            _detailState.update {
                it.copy(
                    isMovieLoading = false,
                    error = null,
                    movies = movies
                )
            }
        }
    }

}

data class DetailState(
    val movieDetail: MovieDetail? = null,
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isMovieLoading: Boolean = false
)