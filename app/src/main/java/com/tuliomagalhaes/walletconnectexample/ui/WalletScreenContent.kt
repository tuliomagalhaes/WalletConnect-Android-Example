package com.tuliomagalhaes.walletconnectexample.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.Surface
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tuliomagalhaes.walletconnectexample.ui.components.TextField
import com.tuliomagalhaes.walletconnectexample.ui.components.TwoSidedButtons

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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AddressInfo(onEvent)
            when (viewState) {
                is ViewState.ConnectionRequest -> AcceptOrRejectConnection(viewState, onEvent)
                is ViewState.EthereumSignIn -> EthereumSignIn(viewState, onEvent)
                ViewState.InitialState -> {}
            }
        }
    }
}

@Composable
private fun AddressInfo(
    onEvent: (Event) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(
            value = "",
            label = "Public Address",
            placeholder = "Input your public address",
        ) { newValue ->
            onEvent(Event.PublicAddressChanged(newValue))
        }
        TextField(
            value = "",
            label = "Private Key",
            placeholder = "Input your address private key",
        ) { newValue ->
            onEvent(Event.PrivateKeyChanged(newValue))
        }
    }
}

@Composable
private fun EthereumSignIn(
    viewState: ViewState.EthereumSignIn,
    onEvent: (Event) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "XYZ wants to sign-in this message below:".format(viewState.dAppUrl),
            textAlign = TextAlign.Center,
        )
        Text(
            text = viewState.signInMessage,
        )
        TwoSidedButtons(
            leftSideButtonText = "Sign",
            rightSideButtonText = "Cancel",
            leftSideButtonClick = {
                onEvent(Event.SignInClicked(viewState.id, viewState.signInMessage))
            },
            rightSideButtonClick = {
                onEvent(Event.CancelSignInClicked(viewState.id))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun AcceptOrRejectConnection(
    viewState: ViewState.ConnectionRequest,
    onEvent: (Event) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "%s wants to connect with your wallet".format(viewState.dAppUrl),
            textAlign = TextAlign.Center,
        )
        TwoSidedButtons(
            leftSideButtonText = "Approve",
            rightSideButtonText = "Reject",
            leftSideButtonClick = {
                onEvent(Event.ApproveClicked)
            },
            rightSideButtonClick = {
                onEvent(Event.RejectClicked)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
