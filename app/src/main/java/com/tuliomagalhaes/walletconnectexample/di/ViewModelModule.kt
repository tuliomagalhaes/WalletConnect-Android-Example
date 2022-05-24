package com.tuliomagalhaes.walletconnectexample.di

import com.tuliomagalhaes.walletconnectexample.WalletConnectViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { WalletConnectViewModel(get()) }
}