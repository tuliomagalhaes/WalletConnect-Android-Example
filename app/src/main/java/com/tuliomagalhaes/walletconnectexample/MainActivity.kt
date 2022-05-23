package com.tuliomagalhaes.walletconnectexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.tuliomagalhaes.walletconnectexample.ui.theme.WalletConnectExampleTheme
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import org.walletconnect.Session
import org.walletconnect.nullOnThrow
import java.lang.IndexOutOfBoundsException

class MainActivity : ComponentActivity(), Session.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletConnectExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Status: ",
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Button(
                                onClick = {
                                    WalletConnectApp.resetSession()
                                    WalletConnectApp.session.addCallback(this@MainActivity)
                                    val i = Intent(Intent.ACTION_VIEW)
                                    i.data = Uri.parse(WalletConnectApp.config.toWCUri())
                                    startActivity(i)
                                },
                            ) {
                                Text(
                                    text = "Connect",
                                )
                            }
                            Button(
                                onClick = {
                                    WalletConnectApp.session.kill()
                                },
                            ) {
                                Text(
                                    text = "Disconnect",
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        try {
            val walletConnectConfig = Session.Config.fromWCUri(intent.data.toString())
            print(walletConnectConfig.bridge)
            print(walletConnectConfig.key)
        } catch (ex: IndexOutOfBoundsException) {
            // TODO: handle invalid uri
        }
    }

    override fun onStatus(status: Session.Status) {
        /*when(status) {
            Session.Status.Approved ->
            Session.Status.Closed ->
            Session.Status.Connected,
            Session.Status.Disconnected,
            is Session.Status.Error -> {
                // Do Stuff
            }
        }*/
    }

    override fun onMethodCall(call: Session.MethodCall) {
    }

    override fun onStart() {
        super.onStart()

        val session = nullOnThrow { WalletConnectApp.session } ?: return
        session.addCallback(this)
    }

    override fun onDestroy() {
        WalletConnectApp.session.removeCallback(this)
        super.onDestroy()
    }
}