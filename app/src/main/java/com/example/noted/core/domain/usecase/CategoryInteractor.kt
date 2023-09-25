package com.example.noted.core.domain.usecase

import com.example.noted.core.domain.model.Category
import com.example.noted.core.domain.repository.ICategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryInteractor @Inject constructor(private val categoryRepository: ICategoryRepository) :
    CategoryUseCase {
    override fun getCategories(): Flow<List<Category>> =
        categoryRepository.getCategories()

    override suspend fun insertCategory(category: Category) =
        categoryRepository.insertCategory(category)

    override suspend fun deleteCategoryAndNotes(categoryName: String) =
        categoryRepository.deleteCategoryAndNotes(categoryName)
}