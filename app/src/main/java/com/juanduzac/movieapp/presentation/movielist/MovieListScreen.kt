package com.juanduzac.movieapp.presentation.movielist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.presentation.ui.theme.MovieAppTheme
import com.juanduzac.movieapp.presentation.ui.theme.Shapes

private const val BaseUrl = "https://image.tmdb.org/t/p/original/"

private val movies = listOf(
    Movie(
        title = "Pelicula",
        posterPath = "wigZBAmNrIhxp2FNGOROUAeHvdh.jpg"
    ),
    Movie(
        title = "Pelicula",
        posterPath = "pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg"
    ),
    Movie(
        title = "Pelicula",
        posterPath = "pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg"
    ),
    Movie(
        title = "Pelicula",
        posterPath = "pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg"
    ),
    Movie(
        title = "Pelicula",
        posterPath = "wigZBAmNrIhxp2FNGOROUAeHvdh.jpg"
    )
)

@Composable
fun MovieListScreen(navController: NavController) {
    Scaffold(
        topBar = { MovieListTopBar("Movies APP") }
    ) {
        LazyColumn(
            Modifier
                .padding(it)
        ) {
            item {
                SubscribedMoviesRowTitle(modifier = Modifier.padding(16.dp))
            }
            item {
                SubscribedMoviesLazyRow(
                    modifier = Modifier.padding(start = 16.dp),
                    movies
                )
            }
            item {
                RecommendedMoviesColumnTitle(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 20.dp,
                        bottom = 16.dp
                    )
                )
            }
            items(movies) { movie ->
                RecommendedMovieCard(movie)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MovieListTopBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 0.dp
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = { SearchIcon() },
        actions = actions,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        modifier = modifier,
        elevation = elevation
    )
}

@Composable
private fun SearchIcon() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
    }
}

@Composable
fun SubscribedMoviesRowTitle(modifier: Modifier) {
    Text(
        modifier = modifier,
        text = "SERIES QUE SIGO"
    )
}

@Composable
fun SubscribedMoviesLazyRow(modifier: Modifier, movies: List<Movie>) {
    LazyRow(modifier = modifier) {
        items(movies) { movie ->
            SubscribedMovieCard(movie)
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
fun RecommendedMoviesColumnTitle(modifier: Modifier) {
    Text(
        modifier = modifier,
        text = "RECOMENDADAS"
    )
}

@Composable
fun RecommendedMovieCard(movie: Movie) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(152.dp),
        shape = Shapes.medium,
        elevation = 4.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BaseUrl + movie.posterPath)
                .crossfade(true)
                .build(),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            Modifier.padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = movie.title ?: " ",
                style = MaterialTheme.typography.h6
            )
        }

    }
}

@Composable
fun SubscribedMovieCard(movie: Movie) {
    Card(
        modifier = Modifier
            .width(92.dp)
            .height(152.dp),
        shape = Shapes.medium,
        elevation = 4.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BaseUrl + movie.posterPath)
                .crossfade(true)
                .build(),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@Preview
private fun PreviewScreen() {
    MovieAppTheme {
        MovieListScreen(
            rememberNavController()
        )
    }
}