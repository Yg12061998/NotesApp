package com.yogigupta1206.notesapp.di

import android.content.Context
import androidx.room.Room
import com.yogigupta1206.notesapp.data.database.AppDatabase
import com.yogigupta1206.notesapp.data.database.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "NotesAppDatabase")
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideNotesDao(appDatabase: AppDatabase): NotesDao {
        return appDatabase.notesDao()
    }
}