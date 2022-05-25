package com.tuliomagalhaes.walletconnectexample

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.walletconnect.Session
import org.walletconnect.impls.FileWCSessionStore
import org.walletconnect.impls.MoshiPayloadAdapter
import org.walletconnect.impls.OkHttpTransport
import org.walletconnect.impls.WCSession
import java.lang.IndexOutOfBoundsException

typealias WalletConnectEventCallback = (WalletConnectEvent) -> Unit

sealed interface WalletConnectEvent {
    object UnhandledRequest : WalletConnectEvent

    object ConnectionRequest : WalletConnectEvent

    data class EthereumSignInRequest(
        val id: Long,
        val message: String,
    ) : WalletConnectEvent
}

class WalletConnectHandler(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient,
    private val fileWCSessionStore: FileWCSessionStore,
) {
    private val openSessions = mutableMapOf<String, Session>()
    private var currentSession: Session? = null

    private val sessionCallback = object: Session.Callback {
        override fun onMethodCall(call: Session.MethodCall) {
            handleSessionCall(call)
        }

        override fun onStatus(status: Session.Status) {
            // no-op
        }
    }

    var walletConnectEventCallback: WalletConnectEventCallback? = null

    fun startSession(uri: String) {
        val config = tryToLoadConfig(uri) ?: return
        val key = config.handshakeTopic

        if (!openSessions.containsKey(key)) {
            val session = WCSession(
                config = config,
                payloadAdapter = MoshiPayloadAdapter(moshi),
                sessionStore = fileWCSessionStore,
                transportBuilder = OkHttpTransport.Builder(okHttpClient, moshi),
                clientMeta = Session.PeerMeta(name = "Example App")
            ).apply {
                addCallback(sessionCallback)
                init()
            }
            openSessions[key] = session
        }

        currentSession = openSessions[key]
    }

    private fun tryToLoadConfig(uri: String): Session.Config? =
        try {
            Session.Config.fromWCUri(uri)
        } catch (ex: IndexOutOfBoundsException) {
            val protocolSeparator = uri.indexOf(':')
            val handshakeTopicSeparator = uri.indexOf('@', startIndex = protocolSeparator)
            val handshakeTopic = uri.substring(protocolSeparator + 1, handshakeTopicSeparator)
            fileWCSessionStore.load(handshakeTopic)?.config
        }

    fun closeSessions() {
        openSessions.forEach { entry ->
            entry.value.apply {
                clearCallbacks()
                kill()
            }
        }
    }

    fun approveConnection(address: String) {
        currentSession?.approve(listOf(address), 1)
    }

    fun rejectConnection() {
        currentSession?.reject()
    }

    fun approveRequest(
        id: Long,
        message: String,
    ) {
        currentSession?.approveRequest(
            id = id,
            response = message,
        )
    }

    fun rejectRequest(id: Long) {
        currentSession?.rejectRequest(
            id = id,
            errorCode = -1,
            errorMsg = "Call Request Rejected",
        )
    }

    private fun handleSessionCall(call: Session.MethodCall) {
        when (call) {
            is Session.MethodCall.SessionRequest -> {
                walletConnectEventCallback?.invoke(WalletConnectEvent.ConnectionRequest)
            }
            is Session.MethodCall.SignMessage -> {
                val event = WalletConnectEvent.EthereumSignInRequest(
                    id = call.id,
                    message = call.message,
                )
                walletConnectEventCallback?.invoke(event)
            }
            is Session.MethodCall.Custom -> {
                if (call.method == "personal_sign" || call.method == "eth_sign") {
                    val event = WalletConnectEvent.EthereumSignInRequest(
                        id = call.id,
                        message = call.params?.joinToString() ?: "",
                    )
                    walletConnectEventCallback?.invoke(event)
                } else {
                    walletConnectEventCallback?.invoke(WalletConnectEvent.UnhandledRequest)
                }
            }
            is Session.MethodCall.SessionUpdate,
            is Session.MethodCall.SendTransaction,
            is Session.MethodCall.Response -> walletConnectEventCallback?.invoke(WalletConnectEvent.UnhandledRequest)
        }
    }
}