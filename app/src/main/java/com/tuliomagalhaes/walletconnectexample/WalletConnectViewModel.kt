package com.tuliomagalhaes.walletconnectexample

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tuliomagalhaes.walletconnectexample.ui.ActionContent
import com.tuliomagalhaes.walletconnectexample.ui.Event
import com.tuliomagalhaes.walletconnectexample.ui.ViewState
import org.kethereum.crypto.CryptoAPI
import org.kethereum.crypto.toAddress
import org.kethereum.eip191.signWithEIP191PersonalSign
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import org.komputing.khex.model.HexString

class WalletConnectViewModel(
    private val walletConnectHandler: WalletConnectHandler,
) : ViewModel() {

    private var ecKeyPair: ECKeyPair

    var viewState: MutableState<ViewState>
        private set

    init {
        walletConnectHandler.walletConnectEventCallback = { event ->
            handleWalletConnectEvent(event)
        }

        ecKeyPair = CryptoAPI.keyPairGenerator.generate()
        viewState = mutableStateOf(
            ViewState(
                walletAddress = ecKeyPair.toAddress().toString(),
                walletPrivateKey = ecKeyPair.privateKey.key.toString(),
                actionContent = ActionContent.Empty,
            )
        )
    }

    fun handleViewEvent(event: Event) {
        when (event) {
            Event.ApproveClicked -> walletConnectHandler.approveConnection(ecKeyPair.toAddress().toString())
            Event.RejectClicked -> walletConnectHandler.rejectConnection()
            is Event.CancelSignInClicked -> walletConnectHandler.rejectRequest(event.id)
            is Event.SignInClicked -> approveSignInRequest(event)
            is Event.PrivateKeyChanged -> onPrivateKeyChanged(event)
        }
    }

    fun startSession(uri: String) {
        walletConnectHandler.startSession(uri)
    }

    fun closeSessions() {
        walletConnectHandler.closeSessions()
    }

    private fun approveSignInRequest(event: Event.SignInClicked) {
        walletConnectHandler.approveRequest(
            id = event.id,
            message = ecKeyPair.signWithEIP191PersonalSign(event.message.toByteArray()).toString(),
        )
    }

    private fun onPrivateKeyChanged(event: Event.PrivateKeyChanged) {
        val newECKeyPair = try {
            val privateKey = PrivateKey(HexString(event.value))
            val publicKey = PublicKey(CryptoAPI.signer.publicFromPrivate(privateKey.key))
            ECKeyPair(
                privateKey = privateKey,
                publicKey = publicKey,
            )
        } catch (ex: Exception) {
            null
        }

        if (newECKeyPair != null) {
            ecKeyPair = newECKeyPair
            viewState.value = viewState.value.copy(
                walletAddress = ecKeyPair.toAddress().toString(),
                walletPrivateKey = ecKeyPair.privateKey.key.toString(),
            )
        } else {
            viewState.value = viewState.value.copy(
                actionContent = ActionContent.Error(
                    message = "Invalid Private Key!",
                )
            )
        }
    }

    private fun handleWalletConnectEvent(event: WalletConnectEvent) {
        when (event) {
            WalletConnectEvent.UnhandledRequest -> {
                viewState.value = viewState.value.copy(
                    actionContent = ActionContent.Empty,
                )
            }
            WalletConnectEvent.ConnectionRequest -> {
                viewState.value = viewState.value.copy(
                    actionContent = ActionContent.ConnectionRequest(
                        dAppUrl = "XYZ",
                    ),
                )
            }
            is WalletConnectEvent.EthereumSignInRequest -> {
                viewState.value = viewState.value.copy(
                    actionContent = ActionContent.EthereumSignIn(
                        id = event.id,
                        dAppUrl = "XYZ",
                        signInMessage = event.message,
                    ),
                )
            }
        }
    }

    override fun onCleared() {
        walletConnectHandler.walletConnectEventCallback = null
    }
}