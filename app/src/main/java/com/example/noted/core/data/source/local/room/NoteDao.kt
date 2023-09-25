package com.example.noted.core.data.source.local.room

import androidx.room.*
import com.example.noted.core.data.source.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM note ORDER BY noteDate DESC")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE noteId =:noteId")
    fun getNoteById(noteId: Int): Flow<NoteEntity>

    @Query("SELECT * FROM note WHERE categoryName =:name ORDER BY noteDate DESC")
    fun getNotesByCategory(name: String): Flow<List<NoteEntity>>

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("SELECT * FROM note WHERE LOWER(noteTitle) LIKE '%' || LOWER(:query) || '%' OR LOWER(noteContent) LIKE '%' || LOWER(:query) || '%' OR LOWER(noteDate) LIKE '%' || LOWER(:query) || '%' ORDER BY noteDate DESC")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM note WHERE categoryName IN ('All', 'Uncategorized')) THEN 1 ELSE 0 END")
    fun checkNotesWithNoCategory(): Flow<Boolean>

    @Query("UPDATE note SET categoryName = :to WHERE categoryName = :from")
    suspend fun changeNotesCategory(from: String, to: String)
}