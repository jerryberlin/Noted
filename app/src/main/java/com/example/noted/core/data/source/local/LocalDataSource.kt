package com.example.noted.core.data.source.local

import com.example.noted.core.data.source.local.entity.CategoryEntity
import com.example.noted.core.data.source.local.entity.NoteEntity
import com.example.noted.core.data.source.local.room.CategoryDao
import com.example.noted.core.data.source.local.room.NoteDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val categoryDao: CategoryDao,
    private val noteDao: NoteDao
) {
    fun getCategories(): Flow<List<CategoryEntity>> = categoryDao.getCategories()

    suspend fun insertCategory(category: CategoryEntity) = categoryDao.insertCategory(category)

    suspend fun deleteCategoryAndNotes(categoryName: String) =
        categoryDao.deleteCategoryAndNotes(categoryName)

    fun getNotes(): Flow<List<NoteEntity>> = noteDao.getNotes()

    fun getNoteById(noteId: Int): Flow<NoteEntity> = noteDao.getNoteById(noteId)

    fun getNotesByCategory(categoryName: String): Flow<List<NoteEntity>> =
        noteDao.getNotesByCategory(categoryName)

    suspend fun updateNote(note: NoteEntity) = noteDao.updateNote(note)

    suspend fun insertNote(note: NoteEntity) = noteDao.insertNote(note)

    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)

    suspend fun deleteNote(note: NoteEntity) = noteDao.deleteNote(note)

    fun checkNotesWithNoCategory(): Flow<Boolean> = noteDao.checkNotesWithNoCategory()

    suspend fun changeNotesCategory(from: String, to: String) =
        noteDao.changeNotesCategory(from, to)
}