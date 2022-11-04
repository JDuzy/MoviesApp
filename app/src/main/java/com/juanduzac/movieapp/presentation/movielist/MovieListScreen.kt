package com.juanduzac.movieapp.presentation.movielist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.presentation.ui.theme.Shapes

private const val BaseUrl = "https://image.tmdb.org/t/p/original/"

@Composable
fun MovieListScreen(navController: NavController, viewModel: MovieListViewModel) {

    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = { MovieListTopBar("Movies APP") }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it),
            state = scrollState
        ) {
            item {
                SubscribedMoviesRowTitle(modifier = Modifier.padding(16.dp))
            }
            item {
                SubscribedMoviesLazyRow(
                    modifier = Modifier.padding(start = 16.dp),
                    viewModel.moviesResponse.movies
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

            itemsIndexed(viewModel.moviesResponse.movies) { index, movie ->
                if (viewModel.shouldFetchMoreMovies(index))
                    viewModel.loadNextMovies()
                RecommendedMovieCard(movie)
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                if (viewModel.isLoading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
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
fun SubscribedMoviesLazyRow(modifier: Modifier, movies: List<Movie>?) {
    movies?.let {
        LazyRow(modifier = modifier) {
            items(movies) { movie ->
                SubscribedMovieCard(movie)
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    } ?: run {
        Text(text = "SIGUE SERIES PARA VERLAS AQUI") // TODO CARD
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
