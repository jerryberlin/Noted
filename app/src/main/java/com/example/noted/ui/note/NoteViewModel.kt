package com.example.noted.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.noted.core.domain.model.Note
import com.example.noted.core.domain.usecase.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteUseCase: NoteUseCase) : ViewModel() {
    val notes = noteUseCase.getNotes().asLiveData()

    private var _notesByCategory = MutableLiveData<List<Note>>()
    var notesByCategory: LiveData<List<Note>> = _notesByCategory

    fun notesByCategory(categoryName: String) {
        notesByCategory = noteUseCase.getNotesByCategory(categoryName).asLiveData()
    }

    suspend fun insertNote(note: Note) {
        noteUseCase.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteUseCase.deleteNote(note)
    }
}