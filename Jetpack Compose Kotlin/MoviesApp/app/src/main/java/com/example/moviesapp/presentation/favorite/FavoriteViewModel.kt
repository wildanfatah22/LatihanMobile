package com.example.moviesapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.MovieRepository
import com.example.moviesapp.model.Movie
import com.example.moviesapp.presentation.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class FavoriteViewModel(
    private val repository: MovieRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Movie>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Movie>>>
        get() = _uiState

    fun getFavoriteMovie() = viewModelScope.launch {
        repository.getFavoriteMovie()
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateMovie(id: Int, newState: Boolean) {
        repository.updateMovie(id, newState)
        getFavoriteMovie()
    }
}