package com.juanduzac.movieapp.presentation.movielist.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.juanduzac.movieapp.presentation.ui.theme.Grey200
import com.juanduzac.movieapp.presentation.ui.theme.Grey300
import com.juanduzac.movieapp.presentation.ui.theme.White

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Search(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true,
    iconTint: Color = Grey200,
    backgroundColor: Color = Grey300,
    onDelete: () -> Unit,
    onFocus: (() -> Unit)? = null
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val searchPadding = 4.dp

    BasicTextField(
        modifier = modifier.then(Modifier
            //.border(1.dp, color, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(Grey300)
            .onFocusChanged {
                if (it.isFocused)
                    onFocus?.let { it() }
            }),
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        textStyle = MaterialTheme.typography.body2.copy(
            color = White,
            lineHeight = TextUnit.Unspecified
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        decorationBox = { innerTextField ->
            CustomSearch(
                value = value,
                placeHolder = placeholder,
                iconTint = iconTint,
                onDelete = onDelete,
                padding = searchPadding
            ) {
                innerTextField()
            }
        },
        singleLine = true
    )
}

@Composable
private fun CustomSearch(
    value: String,
    placeHolder: String,
    iconTint: Color,
    onDelete: () -> Unit,
    padding: Dp,
    innerTextField: @Composable() () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = padding, bottom = padding, end = padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Box {
                Icon(
                    modifier = Modifier
                        .size(21.dp),
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = iconTint
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeHolder,
                        style = MaterialTheme.typography.body2.copy(color = White)
                    )
                }
                innerTextField()
            }
        }

        if (value.isNotEmpty()) {
            IconButton(
                modifier = Modifier
                    .size(21.dp),
                onClick = onDelete
            ) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = null, tint = Grey200)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEqSearch() {
    var textState by remember { mutableStateOf("") }

    Spacer(modifier = Modifier.height(10.dp))
    Search(
        value = textState,
        onValueChange = { textState = it },
        placeholder = "Buscar",
        enabled = true,
        onDelete = { textState = "" }
    )

    Spacer(modifier = Modifier.height(10.dp))
}