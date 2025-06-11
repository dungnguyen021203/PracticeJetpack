package com.example.movieapp.ui.home.components

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    shapes: CornerBasedShape = MaterialTheme.shapes.large,
    bgColor: Color = Color.Black.copy(.8f),
    content: @Composable () -> Unit
) {
    Card(
        shape = shapes,
        colors = CardDefaults.cardColors(
            containerColor = bgColor,
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        content()
    }
}