package com.example.noted.core.data

import com.example.noted.core.data.source.local.LocalDataSource
import com.example.noted.core.domain.model.Category
import com.example.noted.core.domain.repository.ICategoryRepository
import com.example.noted.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) : ICategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return localDataSource.getCategories().map {
            DataMapper.mapCategoryEntitiesToDomain(it)
        }
    }

    override suspend fun insertCategory(category: Category) {
        val categoryEntity = DataMapper.mapCategoryDomainToEntity(category)
        return localDataSource.insertCategory(categoryEntity)
    }

    override suspend fun deleteCategoryAndNotes(categoryName: String) =
        localDataSource.deleteCategoryAndNotes(categoryName)
}