package com.tuliomagalhaes.walletconnectexample.di

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.walletconnect.impls.FileWCSessionStore
import java.io.File

val networkModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        OkHttpClient.Builder().build()
    }

    single {
        val fileStore = File(get<Application>().cacheDir, "session_store.json").apply {
            createNewFile()
        }
        FileWCSessionStore(fileStore, get())
    }
}