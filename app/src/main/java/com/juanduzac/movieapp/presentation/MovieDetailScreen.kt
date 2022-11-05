package com.juanduzac.movieapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanduzac.movieapp.R
import com.juanduzac.movieapp.presentation.ui.theme.Black
import com.juanduzac.movieapp.presentation.ui.theme.HeaderExpandedHeight
import com.juanduzac.movieapp.presentation.ui.theme.MovieAppTheme
import com.juanduzac.movieapp.presentation.ui.theme.White
import com.juanduzac.movieapp.presentation.util.movieExample

private const val BaseUrl = "https://image.tmdb.org/t/p/original/" // TODO Save url on Strings

@Composable
fun MovieDetailScreen() {
    val scrollState = rememberScrollState()
    val progress = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        MotionHeader(
            modifier = Modifier.fillMaxWidth().height(HeaderExpandedHeight),
            progress
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(HeaderExpandedHeight))
            Description()
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun MotionHeader(modifier: Modifier = Modifier, progress: Float) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources.openRawResource(R.raw.motion_scene).readBytes().decodeToString()
    }

    MotionLayout(
        motionScene = MotionScene(motionScene),
        progress = progress,
        modifier = modifier
    ) {
        BackButton(modifier = Modifier
            .padding(top = 16.dp)
            .layoutId("back_button"), onClick = {})
        PosterCard(modifier = Modifier.layoutId("poster_image"))
        Text(
            modifier = Modifier.layoutId("movie_title"),
            text = movieExample.title ?: "", color = Black
        )
        Text(
            modifier = Modifier.layoutId("release_year"),
            text = movieExample.releaseDate ?: "", color = Black
        )
        Button(
            modifier = Modifier.layoutId("subscribe_button"),
            onClick = { /*TODO*/ }) {
            Text(text = "SUSCRIBIRME")
        }
    }

}

@Composable
private fun Description(modifier: Modifier = Modifier) {
    Text(
        modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp),
        text = "OVERVIEW"
    )
    Text(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 32.dp),
        textAlign = TextAlign.Start,
        text = movieExample.overview ?: ""
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
        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Ir atrás")
    }
}

@Composable
private fun PosterCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = 4.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BaseUrl + movieExample.posterPath)
                .crossfade(true)
                .build(),
            placeholder = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    MovieAppTheme {
        MovieDetailScreen()
    }
}