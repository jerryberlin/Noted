package com.example.noted.core.domain.repository

import com.example.noted.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {

    fun getCategories(): Flow<List<Category>>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategoryAndNotes(categoryName: String)
}