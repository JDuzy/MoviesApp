package com.juanduzac.movieapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.juanduzac.movieapp.presentation.movielist.MovieListViewModel
import com.juanduzac.movieapp.presentation.navigation.Navigation
import com.juanduzac.movieapp.presentation.ui.theme.MovieAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadNextMovies()
        viewModel.getSubscribedMovies()
        setContent {
            MovieAppTheme {
                Navigation(navController = rememberNavController())
            }
        }
    }
}
