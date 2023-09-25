package com.example.noted.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noted.core.data.source.local.entity.CategoryEntity
import com.example.noted.core.data.source.local.entity.NoteEntity

@Database(entities = [CategoryEntity::class, NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao
}