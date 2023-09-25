package com.example.noted.core.domain.usecase

import com.example.noted.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryUseCase {
    fun getCategories(): Flow<List<Category>>
    suspend fun insertCategory(category: Category)
    suspend fun deleteCategoryAndNotes(categoryName: String)
}