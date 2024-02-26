package it.thefedex87.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import it.thefedex87.core_ui.theme.LocalSpacing

@Composable
fun ColorPicker(
    onSelectNewColor: (Color) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.Magenta
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier.fillMaxWidth().horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(spacing.spaceSmall)
                .background(Color.Magenta)
                .clickable {
                    onSelectNewColor(Color.Magenta)
                }
                .let {
                    if (selectedColor == Color.Magenta) {
                        return@let it.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RectangleShape
                        )
                    }
                    it
                }
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(spacing.spaceSmall)
                .background(Color.Blue)
                .clickable {
                    onSelectNewColor(Color.Blue)
                }
                .let {
                    if (selectedColor == Color.Blue) {
                        return@let it.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RectangleShape
                        )
                    }
                    it
                }
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(spacing.spaceSmall)
                .background(Color.Green)
                .clickable {
                    onSelectNewColor(Color.Green)
                }
                .let {
                    if (selectedColor == Color.Green) {
                        return@let it.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RectangleShape
                        )
                    }
                    it
                }
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(spacing.spaceSmall)
                .background(Color.Yellow)
                .clickable {
                    onSelectNewColor(Color.Yellow)
                }
                .let {
                    if (selectedColor == Color.Yellow) {
                        return@let it.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RectangleShape
                        )
                    }
                    it
                }
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(spacing.spaceSmall)
                .background(Color.Red)
                .clickable {
                    onSelectNewColor(Color.Red)
                }
                .let {
                    if (selectedColor == Color.Red) {
                        return@let it.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RectangleShape
                        )
                    }
                    it
                }
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(spacing.spaceSmall)
                .background(Color.Cyan)
                .clickable {
                    onSelectNewColor(Color.Cyan)
                }
                .let {
                    if (selectedColor == Color.Cyan) {
                        return@let it.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RectangleShape
                        )
                    }
                    it
                }
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(spacing.spaceSmall)
                .background(Color.Gray)
                .clickable {
                    onSelectNewColor(Color.Gray)
                }
                .let {
                    if (selectedColor == Color.Gray) {
                        return@let it.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RectangleShape
                        )
                    }
                    it
                }
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(spacing.spaceSmall)
                .background(Color.LightGray)
                .clickable {
                    onSelectNewColor(Color.LightGray)
                }
                .let {
                    if (selectedColor == Color.LightGray) {
                        return@let it.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RectangleShape
                        )
                    }
                    it
                }
        )
    }
}