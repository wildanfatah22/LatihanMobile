package com.example.moviesapp.presentation.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviesapp.di.Injection
import com.example.moviesapp.model.Movie
import com.example.moviesapp.presentation.components.EmptyScreen
import com.example.moviesapp.presentation.home.ListMovie
import com.example.moviesapp.presentation.utils.Dimens.MediumPadding1
import com.example.moviesapp.presentation.utils.Dimens.MediumPadding2
import com.example.moviesapp.presentation.utils.UiState
import com.example.moviesapp.presentation.viewmodel.ViewModelFactory


@Composable
fun FavoriteScreen(
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getFavoriteMovie()
            }
            is UiState.Success -> {
                FavoriteList(
                    listMovie = uiState.data,
                    navigateToDetail = navigateToDetail,
                    onFavoriteIconClicked = { id, newState ->
                        viewModel.updateMovie(id, newState)
                    }
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun FavoriteList(
    listMovie: List<Movie>,
    navigateToDetail: (Int) -> Unit,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Favorite",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(MediumPadding2)
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        if (listMovie.isNotEmpty()) {
            ListMovie(
                listMovie = listMovie,
                onFavoriteIconClicked = onFavoriteIconClicked,
                contentPaddingTop = MediumPadding1,
                navigateToDetail = navigateToDetail
            )
        } else {
            EmptyScreen()
        }
    }
}