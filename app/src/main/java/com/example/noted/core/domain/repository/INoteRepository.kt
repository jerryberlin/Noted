package com.example.noted.core.domain.repository

import com.example.noted.core.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface INoteRepository {
    fun getNotes(): Flow<List<Note>>

    fun getNotesByCategory(categoryName: String): Flow<List<Note>>

    fun getNoteById(noteId: Int): Flow<Note>

    suspend fun updateNote(note: Note)

    suspend fun insertNote(note: Note)

    fun searchNote(query: String): Flow<List<Note>>

    suspend fun deleteNote(note: Note)

    fun checkNotesWithNoCategory(): Flow<Boolean>

    suspend fun changeNotesCategory(from: String, to: String)
}