package com.juanduzac.movieapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juanduzac.movieapp.presentation.moviedetail.MovieDetailScreen
import com.juanduzac.movieapp.presentation.movielist.MovieListScreen
import com.juanduzac.movieapp.presentation.movielist.MovieListViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MovieListViewModel = hiltViewModel(),
) {
    NavHost(navController = navController, startDestination = Screen.MovieListScreen.route) {
        composable(route = Screen.MovieListScreen.route) {
            MovieListScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.MovieDetailScreen.route) {
            MovieDetailScreen(
                navController,
                viewModel
            )
        }
    }
}