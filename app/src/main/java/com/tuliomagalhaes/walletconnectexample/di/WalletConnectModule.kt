package com.tuliomagalhaes.walletconnectexample.di

import com.tuliomagalhaes.walletconnectexample.WalletConnectHandler
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val walletConnectModule = module {
    factoryOf(::WalletConnectHandler)
}