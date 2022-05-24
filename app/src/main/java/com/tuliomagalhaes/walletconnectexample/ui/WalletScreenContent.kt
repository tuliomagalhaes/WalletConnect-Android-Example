package com.tuliomagalhaes.walletconnectexample.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

data class ViewState(
    val status: String,
)

sealed interface Event {
    object ApproveClicked : Event
    object RejectClicked : Event
}

@Composable
fun WalletScreenContent(
    viewState: ViewState,
    onEvent: (Event) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = viewState.status,
            )
            AcceptOrRejectConnection(
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun AcceptOrRejectConnection(
    onEvent: (Event) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = {
                onEvent(Event.ApproveClicked)
            },
        ) {
            Text(
                text = "Approve",
            )
        }
        Button(
            onClick = {
                onEvent(Event.RejectClicked)
            },
        ) {
            Text(
                text = "Reject",
            )
        }
    }
}