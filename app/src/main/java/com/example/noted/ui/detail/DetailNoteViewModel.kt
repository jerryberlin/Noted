package com.example.noted.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.noted.core.domain.model.Note
import com.example.noted.core.domain.usecase.CategoryUseCase
import com.example.noted.core.domain.usecase.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase,
    categoryUseCase: CategoryUseCase
) : ViewModel() {

    private var _noteById = MutableLiveData<Note>()
    var noteById: LiveData<Note> = _noteById

    val category = categoryUseCase.getCategories().asLiveData()

    fun getNoteById(noteId: Int) {
        noteById = noteUseCase.getNoteById(noteId).asLiveData()
    }

    suspend fun updateNote(note: Note) =
        noteUseCase.updateNote(note)

    suspend fun insertNote(note: Note) =
        noteUseCase.insertNote(note)
}