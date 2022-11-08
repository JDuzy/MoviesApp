package com.juanduzac.movieapp.presentation.moviedetail.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SubscriptionButton(
    modifier: Modifier = Modifier,
    subscribed: Boolean,
    contentColor: Color,
    mainColor: Color,
    isLoading: Boolean,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier.then(Modifier.fillMaxWidth(0.5f)),
        onClick = onClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (subscribed) contentColor else Color.Transparent,
            contentColor = if (subscribed) mainColor else contentColor
        ),
        shape = CircleShape,
        border = if (subscribed) null else BorderStroke(2.dp, contentColor),
    ) {
        if (!isLoading) {
            Text(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp),
                text = if (subscribed) "SUSCRIPTO" else "SUSCRIBIRME",
                color = if (subscribed) mainColor.copy(alpha = 0.7f) else contentColor
            )
        } else {
            CircularProgressIndicator()
        }
    }
}