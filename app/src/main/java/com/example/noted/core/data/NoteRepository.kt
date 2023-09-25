package com.example.noted.core.data

import com.example.noted.core.data.source.local.LocalDataSource
import com.example.noted.core.domain.model.Note
import com.example.noted.core.domain.repository.INoteRepository
import com.example.noted.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) : INoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return localDataSource.getNotes().map {
            DataMapper.mapNoteEntitiesToDomain(it)
        }
    }

    override fun getNotesByCategory(categoryName: String): Flow<List<Note>> {
        return localDataSource.getNotesByCategory(categoryName).map {
            DataMapper.mapNoteEntitiesToDomain(it)
        }
    }

    override fun getNoteById(noteId: Int): Flow<Note> {
        return localDataSource.getNoteById(noteId).map {
            DataMapper.mapNoteEntityToDomain(it)
        }
    }

    override suspend fun updateNote(note: Note) {
        val noteEntity = DataMapper.mapNoteDomainToEntity(note)
        return localDataSource.updateNote(noteEntity)
    }

    override suspend fun insertNote(note: Note) {
        val noteEntity = DataMapper.mapNoteDomainToEntity(note)
        return localDataSource.insertNote(noteEntity)
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return localDataSource.searchNotes(query).map {
            DataMapper.mapNoteEntitiesToDomain(it)
        }
    }

    override suspend fun deleteNote(note: Note) {
        val noteEntity = DataMapper.mapNoteDomainToEntity(note)
        return localDataSource.deleteNote(noteEntity)
    }

    override fun checkNotesWithNoCategory(): Flow<Boolean> {
        return localDataSource.checkNotesWithNoCategory()
    }

    override suspend fun changeNotesCategory(from: String, to: String) {
        return localDataSource.changeNotesCategory(from, to)
    }
}