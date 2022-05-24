package com.tuliomagalhaes.walletconnectexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tuliomagalhaes.walletconnectexample.ui.theme.WalletConnectExampleTheme
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tuliomagalhaes.walletconnectexample.ui.Event
import com.tuliomagalhaes.walletconnectexample.ui.ViewState
import com.tuliomagalhaes.walletconnectexample.ui.WalletScreenContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WalletConnectViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewState by remember { mutableStateOf(ViewState("Status = ")) }

            WalletConnectExampleTheme {
                WalletScreenContent(
                    viewState = viewState,
                    onEvent = { event ->
                       when (event) {
                           Event.ApproveClicked -> {
                               viewModel.approveConnection()
                           }
                           Event.RejectClicked -> {
                               viewModel.rejectConnection()
                               finish()
                           }
                       }
                    },
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

    private fun handleEvent(event: Event) {

    }
}