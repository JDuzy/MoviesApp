package com.juanduzac.movieapp.presentation.movielist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juanduzac.movieapp.ui.theme.MovieAppTheme

private val movies = listOf("Spider man", "Toy story", "Toy story 2")

@Composable
fun MovieListScreen() {
    Scaffold(
        topBar = { MovieListTopBar() }
    ) {
        LazyColumn(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            item { SubscribedMoviesRowTitle() }
            item { SubscribedMoviesLazyRow() }
            item { RecommendedMoviesColumnTitle() }
            items(movies) {
                RecommendedMovieCard()
            }
        }

    }
}

@Composable
fun MovieListTopBar() {

}

@Composable
fun SubscribedMoviesRowTitle() {
    Text(text = "Series que sigo")
}

@Composable
fun SubscribedMoviesLazyRow() {
    LazyRow{
        items(movies){
            SubscribedMovieCard()
        }
    }
}

@Composable
fun RecommendedMoviesColumnTitle() {
    Text(text = "Recomendadas")
}

@Composable
fun RecommendedMovieCard() {
    Card(

    ) {
        AsyncImage(

        )
    }
}

@Composable
fun SubscribedMovieCard() {

}

@Composable
@Preview
private fun PreviewScreen() {
    MovieAppTheme {
        MovieListScreen()
    }
}