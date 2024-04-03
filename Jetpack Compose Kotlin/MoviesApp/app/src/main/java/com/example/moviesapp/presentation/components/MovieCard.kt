package com.example.moviesapp.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviesapp.R
import com.example.moviesapp.presentation.utils.Dimens.CardSize
import com.example.moviesapp.presentation.utils.Dimens.SmallIconSize
import com.example.moviesapp.presentation.utils.Dimens.SmallPadding1
import com.example.moviesapp.presentation.utils.Dimens.SmallPadding2
import com.example.moviesapp.ui.theme.MoviesAppTheme


@Composable
fun MovieCard(
    id: Int,
    title: String,
    description: String,
    image: Int,
    rating: Double? = null,
    isFavorite: Boolean,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
        ) {
        Image(
            modifier = Modifier
                .size(CardSize)
                .clip(MaterialTheme.shapes.medium),
            painter = painterResource(image),
            contentDescription = "image_movie",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(SmallPadding2))

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = SmallPadding1)
                .height(CardSize)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(SmallPadding1))



            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MovieCardPreview() {
    MoviesAppTheme(dynamicColor = false) {
        MovieCard(
            id = 1,
            title = "The Batman (2022)",
            description = "In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.",
            image = R.drawable.img,
            rating = 4.5,
            isFavorite = false,
            onFavoriteIconClicked = { id, newState -> /* Handle favorite icon click here */ }
        )
    }
}