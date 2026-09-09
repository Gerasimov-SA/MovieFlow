package com.gerasimov_mobile_dev.movieflow.presentation.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.concurrent.timer

@Composable
fun AnimatedSkeletonBox(
    modifier: Modifier,
    cornerRadius: Dp = 0.dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    val color by
    infiniteTransition.animateColor(
        initialValue = Color.Gray.copy(alpha = 0.15f),
        targetValue = Color.Gray.copy(alpha = 0.4f),
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
    )

    Box(
        modifier = modifier
            .background(color)
            .clip(RoundedCornerShape(cornerRadius))
    ){}
}

@Composable
fun SkeletonBox(
    modifier: Modifier,
    cornerRadius: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .background(Color.Gray.copy(alpha = 0.15f))
            .clip(RoundedCornerShape(cornerRadius))
    )
}

@Composable
fun SkeletonRow(
    height: Dp,
    count: Int,
    gap: Dp = 8.dp,
    lastCornerRadius: Dp? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(gap)
    ) {
        repeat(count) { index ->
            val corner =
                if (index == count - 1 && lastCornerRadius != null) lastCornerRadius else 4.dp
            AnimatedSkeletonBox(
                modifier = Modifier
                    .height(height)
                    .weight(1f),
                cornerRadius = corner
            )
        }
    }
}



