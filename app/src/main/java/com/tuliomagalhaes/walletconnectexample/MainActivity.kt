package com.tuliomagalhaes.walletconnectexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tuliomagalhaes.walletconnectexample.ui.theme.WalletConnectExampleTheme
import android.content.Intent
import com.tuliomagalhaes.walletconnectexample.ui.WalletScreenContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WalletConnectViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WalletConnectExampleTheme {
                WalletScreenContent(
                    viewState = viewModel.viewState.value,
                    onEvent = viewModel::handleViewEvent,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val uri = intent.data.toString()
        viewModel.startSession(uri)
    }

    override fun onDestroy() {
        viewModel.closeSessions()
        super.onDestroy()
    }
}