package com.tuliomagalhaes.walletconnectexample.ui

data class ViewState(
    val walletAddress: String,
    val walletPrivateKey: String,
    val actionContent: ActionContent,
)

sealed interface ActionContent {
    object Empty : ActionContent

    data class Error(
        val message: String,
    ) : ActionContent

    data class ConnectionRequest(
        val dAppUrl: String,
    ) : ActionContent

    data class EthereumSignIn(
        val id: Long,
        val dAppUrl: String,
        val signInMessage: String
    ) : ActionContent
}

sealed interface Event {
    data class PrivateKeyChanged(
        val value: String,
    ) : Event

    object ApproveClicked : Event
    object RejectClicked : Event

    data class CancelSignInClicked(
        val id: Long,
    ) : Event
    data class SignInClicked(
        val id: Long,
        val message: String,
    ) : Event
}