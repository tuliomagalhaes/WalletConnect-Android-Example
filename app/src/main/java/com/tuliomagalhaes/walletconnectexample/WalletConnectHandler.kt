package com.tuliomagalhaes.walletconnectexample

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.walletconnect.Session
import org.walletconnect.impls.FileWCSessionStore
import org.walletconnect.impls.MoshiPayloadAdapter
import org.walletconnect.impls.OkHttpTransport
import org.walletconnect.impls.WCSession
import java.lang.IndexOutOfBoundsException

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
            handleSessionStatus(status)
        }
    }

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

    fun approveConnection() {
        // TODO: get address from input field
        currentSession?.approve(listOf(""), 1)
    }

    fun rejectConnection() {
        currentSession?.reject()
    }

    private fun handleSessionCall(call: Session.MethodCall) {
        print(call)
    }

    private fun handleSessionStatus(status: Session.Status) {
        print(status)
    }
}