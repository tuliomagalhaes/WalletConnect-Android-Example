package com.tuliomagalhaes.walletconnectexample

import android.app.Application
import com.tuliomagalhaes.walletconnectexample.di.networkModule
import com.tuliomagalhaes.walletconnectexample.di.viewModelModule
import com.tuliomagalhaes.walletconnectexample.di.walletConnectModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WalletConnectApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WalletConnectApp)
            modules(networkModule, walletConnectModule, viewModelModule)
        }
    }
}