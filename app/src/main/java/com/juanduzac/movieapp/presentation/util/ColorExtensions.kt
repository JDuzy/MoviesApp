package com.juanduzac.movieapp.presentation.util

import androidx.compose.ui.graphics.Color

// The definition of luminance for digital formats. en.wikipedia.org/wiki/Luma_%28video%29
fun Color.isDark(): Boolean {
    val darkness: Double =
        1 - (0.299 * this.red + 0.587 * this.green + 0.114 * this.blue) / 255
    return darkness >= 0.5
}