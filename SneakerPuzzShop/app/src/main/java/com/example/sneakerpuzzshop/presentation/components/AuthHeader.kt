package com.example.sneakerpuzzshop.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sneakerpuzzshop.R

@Composable
fun AuthHeader(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login_animation))

    val progress by animateLottieCompositionAsState(
        isPlaying = true,
        composition = composition,
        speed = 0.7f
    )

    LottieAnimation(
        modifier = modifier.size(300.dp),
        composition = composition,
        progress = {progress}
    )
}