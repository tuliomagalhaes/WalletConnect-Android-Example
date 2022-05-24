package com.tuliomagalhaes.walletconnectexample

import androidx.lifecycle.ViewModel

class WalletConnectViewModel(
    private val walletConnectHandler: WalletConnectHandler,
) : ViewModel() {

    fun approveConnection() {
        walletConnectHandler.approveConnection()
    }

    fun rejectConnection() {
        walletConnectHandler.rejectConnection()
    }

    fun startSession(uri: String) {
        walletConnectHandler.startSession(uri)
    }

    fun closeSessions() {
        walletConnectHandler.closeSessions()
    }
}