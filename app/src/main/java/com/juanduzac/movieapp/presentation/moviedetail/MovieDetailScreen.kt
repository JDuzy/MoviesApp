package com.juanduzac.movieapp.presentation.moviedetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanduzac.movieapp.R
import com.juanduzac.movieapp.domain.model.Movie
import com.juanduzac.movieapp.presentation.movielist.MovieListViewModel
import com.juanduzac.movieapp.presentation.ui.theme.Black
import com.juanduzac.movieapp.presentation.ui.theme.HeaderExpandedHeight
import com.juanduzac.movieapp.presentation.ui.theme.MovieAppTheme
import com.juanduzac.movieapp.presentation.ui.theme.White
import kotlin.math.max
import kotlin.math.min

private const val BaseUrl = "https://image.tmdb.org/t/p/original/" // TODO Save url on Strings

@Composable
fun MovieDetailScreen(navController: NavController, viewModel: MovieListViewModel) {
    val scrollState = rememberScrollState()

    val headerHeight = HeaderExpandedHeight

    val maxOffset = with(LocalDensity.current) {
        headerHeight.roundToPx()
    } // - LocalWindowInsets.current.systemBars.layoutInsets.top

    val offset = min(scrollState.value, maxOffset)

    val offsetProgression = max(0f, offset.toFloat()) / maxOffset

    // val progress = if (maxValue == 0f) 0f else scrollState.value.toFloat() / maxValue

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackgroundPoster(viewModel.selectedMovie)
        Content(scrollState, viewModel.selectedMovie, offsetProgression)
        MotionHeader(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeaderExpandedHeight),
            offsetProgression,
            viewModel.selectedMovie
        ) { navController.popBackStack() }

    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun MotionHeader(
    modifier: Modifier = Modifier,
    progress: Float,
    movie: Movie,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources.openRawResource(R.raw.motion_scene).readBytes().decodeToString()
    }

    var sub by remember { mutableStateOf(true) }

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
            text = movie.title ?: "", color = White,
        )
        Text(
            modifier = Modifier.layoutId("release_year"),
            text = movie.releaseDate ?: "", color = White
        )
        SubscribeButton(
            modifier = Modifier.layoutId("subscribe_button"),
            onClick = { sub = !sub },
            subscribed = sub,
        )
    }

}

@Composable
private fun Content(scrollState: ScrollState, movie: Movie, progress: Float) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = HeaderExpandedHeight),

        ) {
        Description(movie = movie)
    }
}

@Composable
private fun BackgroundPoster(movie: Movie) {
    Box(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = RectangleShape
                ) // TODO Main color
        )
    }

}

@Composable
private fun Description(modifier: Modifier = Modifier, movie: Movie) {
    Text(
        modifier = Modifier
            .padding(top = 40.dp, start = 32.dp, end = 32.dp)
            .layoutId("overview"),
        text = "OVERVIEW",
        color = White
    )
    Text(
        modifier = modifier
            .padding(vertical = 24.dp, horizontal = 32.dp)
            .layoutId("description"),
        textAlign = TextAlign.Start,
        text = movie.overview ?: "",
        color = White,
        style = MaterialTheme.typography.body1,
        lineHeight = 30.sp
    )
}

@Composable
private fun SubscribeButton(
    modifier: Modifier = Modifier,
    subscribed: Boolean,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (subscribed) White else Color.Transparent,
            contentColor = if (subscribed) Black else White // TODO Main color
        ),
        shape = CircleShape,
        border = if (subscribed) null else BorderStroke(2.dp, White),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp),
            text = if (subscribed) "SUSCRIPTO" else "SUSCRIBIRME"
        )
    }
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
        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Ir atrás")
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

