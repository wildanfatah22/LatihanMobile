package com.example.moviesapp.presentation.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviesapp.R
import com.example.moviesapp.di.Injection
import com.example.moviesapp.model.Movie
import com.example.moviesapp.presentation.components.EmptyScreen
import com.example.moviesapp.presentation.components.MovieCard
import com.example.moviesapp.presentation.components.SearchBar
import com.example.moviesapp.presentation.utils.Dimens.MediumPadding1
import com.example.moviesapp.presentation.utils.Dimens.MediumPadding2
import com.example.moviesapp.presentation.utils.UiState
import com.example.moviesapp.presentation.viewmodel.ViewModelFactory

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Int) -> Unit,
) {
    val query by viewModel.query
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.search(query)
            }
            is UiState.Success -> {
                HomeContent(
                    query = query,
                    onValueChange = viewModel::search,
                    listMovie = uiState.data,
                    onFavoriteIconClicked = { id, newState ->
                        viewModel.updateMovie(id, newState)
                    },
                    navigateToDetail = navigateToDetail
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    query: String,
    onValueChange: (String) -> Unit,
    listMovie: List<Movie>,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MediumPadding1)
            .statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
                .padding(horizontal = MediumPadding1)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        SearchBar(
            modifier = Modifier
                .padding(horizontal = MediumPadding2)
                .fillMaxWidth(),
            query = query,
            readOnly = false,
            onValueChange = onValueChange,
        )

        Spacer(modifier = Modifier.height(MediumPadding1))


        if (listMovie.isNotEmpty()) {
            ListMovie(
                listMovie = listMovie,
                onFavoriteIconClicked = onFavoriteIconClicked,
                navigateToDetail = navigateToDetail
            )
        } else {
            EmptyScreen(
                modifier = Modifier
                    .testTag("emptyList")

            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListMovie(
    listMovie: List<Movie>,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    contentPaddingTop: Dp = 0.dp,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = MediumPadding2,
            end = MediumPadding2,
            bottom = MediumPadding2,
            top = contentPaddingTop
        ),
        verticalArrangement = Arrangement.spacedBy(MediumPadding2),
        modifier = modifier
            .testTag("lazy_list")
    ) {
        items(listMovie, key = { it.id }) { item ->
            MovieCard(
                id = item.id,
                title = item.title,
                description = item.description,
                image = item.image,
                rating = item.rating,
                isFavorite = item.isFavorite,
                onFavoriteIconClicked = onFavoriteIconClicked,
                modifier = Modifier
                    .animateItemPlacement(tween(durationMillis = 200))
                    .clickable { navigateToDetail(item.id) }
            )
        }
    }
}
