package com.tuliomagalhaes.walletconnectexample.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TwoSidedButtons(
    leftSideButtonText: String,
    rightSideButtonText: String,
    leftSideButtonClick: () -> Unit,
    rightSideButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = leftSideButtonClick,
        ) {
            Text(
                text = leftSideButtonText,
            )
        }
        Button(
            onClick = rightSideButtonClick,
        ) {
            Text(
                text = rightSideButtonText,
            )
        }
    }
}