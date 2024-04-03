package com.example.moviesapp.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.MovieRepository
import com.example.moviesapp.model.Movie
import com.example.moviesapp.presentation.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Movie>>>
    get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) = viewModelScope.launch {
        _query.value = newQuery
        repository.searchMovie(_query.value)
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateMovie(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateMovie(id, newState)
            .collect { isUpdated ->
                if (isUpdated) search(_query.value)
            }
    }
}