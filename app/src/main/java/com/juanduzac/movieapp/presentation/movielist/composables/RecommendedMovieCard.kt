package com.juanduzac.movieapp.presentation.movielist.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanduzac.movieapp.R
import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.presentation.ui.theme.DarkBlue
import com.juanduzac.movieapp.presentation.ui.theme.LightBlue
import com.juanduzac.movieapp.presentation.ui.theme.Shapes

const val RecommendedMovieCardTestTag = "RecommendedMovieCard"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecommendedMovieCard(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(152.dp)
            .testTag(RecommendedMovieCardTestTag),
        shape = Shapes.medium,
        elevation = 4.dp,
        onClick = onClick
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(stringResource(id = R.string.image_base_url)  + movie.posterPath)
                .crossfade(true)
                .build(),
            colorFilter = ColorFilter.tint(color = DarkBlue, BlendMode.Color),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),

            )

        Box(
            Modifier.padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = movie.title ?: " ",
                style = MaterialTheme.typography.h6,
                color = LightBlue
            )
        }

    }
}