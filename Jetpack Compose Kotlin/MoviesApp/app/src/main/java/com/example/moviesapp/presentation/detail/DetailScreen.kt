package com.example.moviesapp.presentation.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviesapp.R
import com.example.moviesapp.di.Injection
import com.example.moviesapp.presentation.utils.Dimens.MediumPadding2
import com.example.moviesapp.presentation.utils.Dimens.SmallPadding2
import com.example.moviesapp.presentation.utils.UiState
import com.example.moviesapp.presentation.viewmodel.ViewModelFactory

@Composable
fun DetailScreen(
    movieId: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getMovieById(movieId)
            }

            is UiState.Success -> {
                val data = uiState.data
                DetailInformation(
                    image = data.image,
                    id = data.id,
                    title = data.title,
                    description = data.description,
                    rating = data.rating,
                    isFavorite = data.isFavorite,
                    navigateBack = navigateBack,
                    onFavoriteButtonClicked = { id, state ->
                        viewModel.updateMovie(id, state)
                    }
                )
            }

            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInformation(
    id: Int,
    title: String,
    @DrawableRes image: Int,
    description: String,
    rating: Double?,
    isFavorite: Boolean,
    navigateBack: () -> Unit,
    onFavoriteButtonClicked: (id: Int, state: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = MediumPadding2)
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Transparent,
                    actionIconContentColor = Color.Gray,
                    navigationIconContentColor = Color.Gray
                ),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack,
                        modifier = Modifier
                            .testTag("back_home")
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {

                    IconButton(
                        onClick = {
                            onFavoriteButtonClicked(id, isFavorite)
                        },
                        modifier = Modifier
                            .testTag("favorite_detail_button")
                    ) {
                        Icon(
                            imageVector = if (!isFavorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                            contentDescription = if (!isFavorite) stringResource(R.string.add_favorite) else stringResource(
                                R.string.delete_favorite
                            ),
                            tint = if (!isFavorite) MaterialTheme.colorScheme.primary else Color.Red
                        )
                    }
                },
            )
            Image(
                painter = painterResource(image),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scrollToBottom")
            )
            Spacer(modifier = Modifier.height(MediumPadding2))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MediumPadding2)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = MediumPadding2)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier
                            .size(MediumPadding2)
                    )
                    Text(
                        text = rating.toString(),
                        modifier = Modifier
                            .padding(start = 2.dp, end = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(SmallPadding2))

                Divider(
                    modifier = Modifier.padding(
                        SmallPadding2
                    )
                )
                Text(
                    text = description,
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    modifier = Modifier
                        .padding(horizontal = SmallPadding2)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}