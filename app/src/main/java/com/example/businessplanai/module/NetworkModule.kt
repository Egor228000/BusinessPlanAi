package com.example.businessplanai.module

import android.content.Context
import android.net.ConnectivityManager
import com.example.businessplanai.NetworkStatusTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNetworkStatusTracker(
        connectivityManager: ConnectivityManager
    ): NetworkStatusTracker {
        return NetworkStatusTracker(connectivityManager)
    }
}