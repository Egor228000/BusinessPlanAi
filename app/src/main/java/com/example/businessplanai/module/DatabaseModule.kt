package com.example.businessplanai.module

import android.content.Context
import androidx.room.Room
import com.example.businessplanai.data.AppDatabase
import com.example.businessplanai.data.BusinessDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideBusinessDao(database: AppDatabase): BusinessDao = database.businessDao()
}