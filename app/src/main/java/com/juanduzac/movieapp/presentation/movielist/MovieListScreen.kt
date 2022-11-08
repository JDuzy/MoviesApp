package com.juanduzac.movieapp.presentation.movielist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.presentation.movielist.composables.Search
import com.juanduzac.movieapp.presentation.navigation.Screen
import com.juanduzac.movieapp.presentation.ui.theme.Black
import com.juanduzac.movieapp.presentation.ui.theme.DarkBlue
import com.juanduzac.movieapp.presentation.ui.theme.LightBlue
import com.juanduzac.movieapp.presentation.ui.theme.Shapes
import com.juanduzac.movieapp.presentation.ui.theme.White
import kotlinx.coroutines.launch

private const val BaseUrl = "https://image.tmdb.org/t/p/original/"

@Composable
fun MovieListScreen(navController: NavController, viewModel: MovieListViewModel) {

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            with(viewModel) {
                MovieListTopBar(
                    "Movies APP",
                    isSearching = isSearching,
                    searchQuery = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        searchMovies()
                    },
                    cancelSearch = {
                        isSearching = false
                        cancelSearch()
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(index = 0)
                        }
                    },
                    onClickNavIcon = { isSearching = true },
                    onDeleteSearch = { searchQuery = "" }
                )
            }
        }
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    viewModel.subscribedMovies,
                    viewModel.isLoadingRow
                ){ movie ->
                    viewModel.selectMovie(movie)
                    navController.navigate(Screen.MovieDetailScreen.route)
                }
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
                RecommendedMovieCard(movie) {
                    viewModel.selectMovie(movie)
                    navController.navigate(Screen.MovieDetailScreen.route)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                if (viewModel.isLoadingColum) {
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
    backgroundColor: Color = Black,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 0.dp,
    isSearching: Boolean,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    cancelSearch: () -> Unit,
    onClickNavIcon: () -> Unit,
    onDeleteSearch: () -> Unit
) {
    TopAppBar(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        modifier = modifier,
        elevation = elevation
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isSearching) {
                    SearchIcon(onClickNavIcon, White)
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = title,
                        color = White
                    )
                } else {
                    Search(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 12.dp)
                            .fillMaxWidth(0.8f),
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        onDelete = onDeleteSearch,
                        placeholder = "Buscar"
                    )
                    Text(
                        modifier = Modifier
                            .clickable { cancelSearch() }
                            .padding(start = 12.dp, bottom = 12.dp),
                        text = "Cancel",
                        color = White
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchIcon(onClick: () -> Unit, color: Color) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Rounded.Search, contentDescription = null, tint = color)
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
fun SubscribedMoviesLazyRow(modifier: Modifier, movies: List<Movie>, isLoading: Boolean, onClickMovie: (Movie) -> Unit) {
    LazyRow(modifier = modifier) {
        if (isLoading) {
            item {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
        } else {
            if (movies.isEmpty()) {
                item {
                    EmptySubscriptionList()
                }
            } else {
                items(movies) { movie ->
                    SubscribedMovieCard(movie, onClickMovie)
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecommendedMovieCard(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(152.dp),
        shape = Shapes.medium,
        elevation = 4.dp,
        onClick = onClick
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BaseUrl + movie.posterPath)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubscribedMovieCard(movie: Movie, onClick: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .width(92.dp)
            .height(152.dp),
        shape = Shapes.medium,
        elevation = 4.dp,
        onClick = {
           onClick(movie)
        }
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
private fun EmptySubscriptionList() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(152.dp),
        shape = Shapes.medium,
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Suscribete a peliculas para organizarlas aquí",
                style = MaterialTheme.typography.body1,
                color = LightBlue
            )
        }

    }
}
