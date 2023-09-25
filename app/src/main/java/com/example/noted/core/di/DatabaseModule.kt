package com.example.noted.core.di

import android.content.Context
import androidx.room.Room
import com.example.noted.core.data.source.local.room.AppDatabase
import com.example.noted.core.data.source.local.room.CategoryDao
import com.example.noted.core.data.source.local.room.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val databaseFile = context.getDatabasePath("app_database.db")

        if (!databaseFile.exists()) {
            try {
                val inputStream = context.assets.open("database/category.db")
                val outputStream = FileOutputStream(databaseFile)

                inputStream.copyTo(outputStream)

                inputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database.db",
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()
}