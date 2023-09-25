package com.example.noted.core.domain.usecase

import com.example.noted.core.domain.model.Note
import com.example.noted.core.domain.repository.INoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteInteractor @Inject constructor(private val noteRepository: INoteRepository) :
    NoteUseCase {
    override fun getNotes(): Flow<List<Note>> =
        noteRepository.getNotes()

    override fun getNotesByCategory(categoryName: String): Flow<List<Note>> =
        noteRepository.getNotesByCategory(categoryName)

    override fun getNoteById(noteId: Int): Flow<Note> =
        noteRepository.getNoteById(noteId)

    override suspend fun updateNote(note: Note) =
        noteRepository.updateNote(note)

    override suspend fun insertNote(note: Note) =
        noteRepository.insertNote(note)

    override fun searchNote(query: String): Flow<List<Note>> =
        noteRepository.searchNote(query)

    override suspend fun deleteNote(note: Note) =
        noteRepository.deleteNote(note)

    override fun checkNotesWithNoCategory(): Flow<Boolean> =
        noteRepository.checkNotesWithNoCategory()

    override suspend fun changeNotesCategory(from: String, to: String) =
        noteRepository.changeNotesCategory(from, to)
}