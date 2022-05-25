package com.tuliomagalhaes.walletconnectexample

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tuliomagalhaes.walletconnectexample.ui.Event
import com.tuliomagalhaes.walletconnectexample.ui.ViewState

class WalletConnectViewModel(
    private val walletConnectHandler: WalletConnectHandler,
) : ViewModel() {

    private var publicAddress: String = ""
    private var privateKey: String = ""

    var viewState: MutableState<ViewState> = mutableStateOf(ViewState.InitialState)
        private set

    init {
        walletConnectHandler.walletConnectEventCallback = { event ->
            handleWalletConnectEvent(event)
        }
    }

    fun handleViewEvent(event: Event) {
        when (event) {
            Event.ApproveClicked -> walletConnectHandler.approveConnection(publicAddress)
            Event.RejectClicked -> walletConnectHandler.rejectConnection()
            is Event.CancelSignInClicked -> walletConnectHandler.rejectRequest(event.id)
            is Event.SignInClicked -> {
                walletConnectHandler.approveRequest(
                    id = event.id,
                    message = event.message, // TODO: sign request
                )
            }
            is Event.PublicAddressChanged -> {
                publicAddress = event.value
            }
            is Event.PrivateKeyChanged -> {
                privateKey = event.value
            }
        }
    }

    private fun handleWalletConnectEvent(event: WalletConnectEvent) {
        when (event) {
            WalletConnectEvent.UnhandledRequest -> {
                viewState.value = ViewState.InitialState
            }
            WalletConnectEvent.ConnectionRequest -> {
                viewState.value = ViewState.ConnectionRequest(
                    dAppUrl = "XYZ",
                )
            }
            is WalletConnectEvent.EthereumSignInRequest -> {
                viewState.value = ViewState.EthereumSignIn(
                    id = event.id,
                    dAppUrl = "XYZ",
                    signInMessage = event.message,
                )
            }
        }
    }

    fun startSession(uri: String) {
        walletConnectHandler.startSession(uri)
    }

    fun closeSessions() {
        walletConnectHandler.closeSessions()
    }
}