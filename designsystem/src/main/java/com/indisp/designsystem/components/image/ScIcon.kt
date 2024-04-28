package com.indisp.designsystem.components.image

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.indisp.designsystem.components.text.ImageConfig

@Composable
fun ScIcon(
    config: ImageConfig,
    tintColor: Color,
    modifier: Modifier
) {
    when (config) {
        is ImageConfig.Paint -> {
            Icon(
                painter = config.painter,
                contentDescription = "suffixIcon",
                tint = tintColor,
                modifier = modifier
            )
        }

        is ImageConfig.Vector -> {
            Icon(
                imageVector = config.vector,
                contentDescription = "suffixIcon",
                tint = tintColor,
                modifier = modifier
            )
        }

        null -> {}
    }
}