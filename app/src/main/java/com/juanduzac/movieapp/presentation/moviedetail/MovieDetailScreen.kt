package com.juanduzac.movieapp.presentation.moviedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanduzac.movieapp.R
import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.presentation.moviedetail.composables.SubscriptionButton
import com.juanduzac.movieapp.presentation.movielist.MovieListViewModel
import com.juanduzac.movieapp.presentation.ui.theme.Black
import com.juanduzac.movieapp.presentation.ui.theme.HeaderCollapsedHeight
import com.juanduzac.movieapp.presentation.ui.theme.HeaderExpandedHeight
import com.juanduzac.movieapp.presentation.ui.theme.White
import kotlin.math.max

@Composable
fun MovieDetailScreen(navController: NavController, viewModel: MovieListViewModel) {
    val scrollState = rememberLazyListState()

    val maxOffset = with(LocalDensity.current) {
        (HeaderExpandedHeight - HeaderCollapsedHeight).toPx()
    }

    var mainColor by remember { mutableStateOf(Black) }
    val contentColor = if (mainColor.luminance() >= 0.5f) Black else White

    val progress = (max(
        0f,
        (scrollState.firstVisibleItemScrollOffset.toFloat())
    ) / maxOffset).takeIf { it <= 1 } ?: 1f

    with(viewModel) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BackgroundPoster(selectedMovie, mainColor) {
                mainColor = it
            }
            Content(scrollState, selectedMovie, progress, contentColor)
            MotionHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HeaderExpandedHeight),
                progress = progress,
                movie = selectedMovie,
                isMovieSubscribed = selectedMovie.wasSubscribed,
                contentColor = contentColor,
                mainColor = mainColor,
                isLoadingRow = isLoadingRow,
                onSubscribeButtonClick = {
                    onSubscribeButtonClick()
                }
            ) { navController.popBackStack() }
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun MotionHeader(
    modifier: Modifier = Modifier,
    progress: Float,
    movie: Movie,
    isMovieSubscribed: Boolean,
    contentColor: Color,
    mainColor: Color,
    isLoadingRow: Boolean,
    onSubscribeButtonClick: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources.openRawResource(R.raw.motion_scene).readBytes().decodeToString()
    }

    MotionLayout(
        motionScene = MotionScene(motionScene),
        progress = progress,
        modifier = modifier
    ) {
        BackButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .layoutId("back_button"),
            onClick = onBack
        )
        PosterCard(modifier = Modifier.layoutId("poster_image"), movie)
        Text(
            modifier = Modifier.layoutId("movie_title"),
            text = movie.title ?: "",
            color = contentColor,
            style = MaterialTheme.typography.h4
        )
        Text(
            modifier = Modifier.layoutId("release_year"),
            text = movie.getReleaseYearText(),
            color = contentColor
        )
        SubscriptionButton(
            modifier = Modifier.layoutId("subscribe_button"),
            onClick = onSubscribeButtonClick,
            subscribed = isMovieSubscribed,
            contentColor = contentColor,
            isLoading = isLoadingRow,
            mainColor = mainColor
        )
    }

}

@Composable
private fun Content(
    scrollState: LazyListState,
    movie: Movie,
    progress: Float,
    contentColor: Color
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(top = HeaderExpandedHeight),
        state = scrollState
    ) {
        item {
            Description(movie = movie, contentColor = contentColor)
        }
    }
}

@Composable
private fun BackgroundPoster(movie: Movie, mainColor: Color, onMainColorChange: (Color) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(stringResource(id = R.string.image_base_url) + movie.posterPath)
                .crossfade(true)
                .allowHardware(false)
                .build(),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onSuccess = { result ->
                Palette.Builder(result.result.drawable.toBitmap()).generate() { palette ->
                    palette?.dominantSwatch?.let {
                        onMainColorChange(Color(it.rgb))
                    }
                }
            },
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = mainColor.copy(alpha = 0.75f),
                    shape = RectangleShape
                )
        )
    }
}

@Composable
private fun Description(modifier: Modifier = Modifier, movie: Movie, contentColor: Color) {
    Text(
        modifier = Modifier
            .padding(top = 40.dp, start = 32.dp, end = 32.dp)
            .layoutId("overview"),
        text = "OVERVIEW",
        color = contentColor
    )
    Text(
        modifier = modifier
            .padding(vertical = 24.dp, horizontal = 32.dp)
            .layoutId("description"),
        textAlign = TextAlign.Start,
        text = movie.overview ?: "",
        color = contentColor,
        style = MaterialTheme.typography.body1,
        lineHeight = 30.sp
    )
}

@Composable
private fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = Black,
    contentColor: Color = White
) {
    Button(
        modifier = modifier.then(Modifier.size(32.dp)),
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Ir atr??s")
    }
}

@Composable
private fun PosterCard(modifier: Modifier = Modifier, movie: Movie) {
    Card(
        modifier = modifier,
        elevation = 4.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(stringResource(id = R.string.image_base_url) + movie.posterPath)
                .crossfade(true)
                .build(),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
