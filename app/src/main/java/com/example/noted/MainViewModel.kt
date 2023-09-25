package com.example.noted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.noted.core.domain.model.Category
import com.example.noted.core.domain.model.Note
import com.example.noted.core.domain.usecase.CategoryUseCase
import com.example.noted.core.domain.usecase.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase,
    private val noteUseCase: NoteUseCase
) : ViewModel() {
    private var _notes = MutableLiveData<List<Note>>()
    var notes: LiveData<List<Note>> = _notes

    val category = categoryUseCase.getCategories().asLiveData()

    suspend fun insertCategory(category: Category) =
        categoryUseCase.insertCategory(category)


    suspend fun deleteCategoryAndNotes(categoryName: String) =
        categoryUseCase.deleteCategoryAndNotes(categoryName)


    fun searchNotes(query: String) {
        notes = noteUseCase.searchNote(query).asLiveData()
    }

    suspend fun deleteNote(note: Note) {
        noteUseCase.deleteNote(note)
    }

    fun checkNotesWithNoCategory(): LiveData<Boolean> {
        return noteUseCase.checkNotesWithNoCategory().asLiveData()
    }

    suspend fun changeNotesCategory(from: String, to: String) =
        noteUseCase.changeNotesCategory(from, to)

}