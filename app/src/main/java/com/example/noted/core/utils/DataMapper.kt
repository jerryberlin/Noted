package com.example.noted.core.utils

import com.example.noted.core.data.source.local.entity.CategoryEntity
import com.example.noted.core.data.source.local.entity.NoteEntity
import com.example.noted.core.domain.model.Category
import com.example.noted.core.domain.model.Note

object DataMapper {
    fun mapCategoryEntitiesToDomain(input: List<CategoryEntity>): List<Category> =
        input.map {
            Category(
                categoryName = it.categoryName
            )
        }

    fun mapCategoryDomainToEntity(input: Category): CategoryEntity =
        CategoryEntity(
            categoryName = input.categoryName
        )

    fun mapNoteEntitiesToDomain(input: List<NoteEntity>): List<Note> =
        input.map {
            Note(
                noteId = it.noteId,
                noteTitle = it.noteTitle,
                noteContent = it.noteContent,
                noteDate = it.noteDate,
                noteColor = it.noteColor,
                categoryName = it.categoryName
            )
        }

    fun mapNoteDomainToEntity(input: Note): NoteEntity =
        NoteEntity(
            noteId = input.noteId,
            noteTitle = input.noteTitle,
            noteContent = input.noteContent,
            noteDate = input.noteDate,
            noteColor = input.noteColor,
            categoryName = input.categoryName
        )

    fun mapNoteEntityToDomain(input: NoteEntity): Note =
        Note(
            noteId = input.noteId,
            noteTitle = input.noteTitle,
            noteContent = input.noteContent,
            noteDate = input.noteDate,
            noteColor = input.noteColor,
            categoryName = input.categoryName

        )
}