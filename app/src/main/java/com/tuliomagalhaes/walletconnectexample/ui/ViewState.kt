package com.tuliomagalhaes.walletconnectexample.ui

sealed interface ViewState {
    object InitialState : ViewState

    data class ConnectionRequest(
        val dAppUrl: String,
    ) : ViewState

    data class EthereumSignIn(
        val id: Long,
        val dAppUrl: String,
        val signInMessage: String
    ) : ViewState
}

sealed interface Event {
    data class PublicAddressChanged(
        val value: String,
    ) : Event
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