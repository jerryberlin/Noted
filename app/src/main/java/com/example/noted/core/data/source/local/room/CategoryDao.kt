package com.example.noted.core.data.source.local.room

import androidx.room.*
import com.example.noted.core.data.source.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("DELETE FROM category WHERE categoryName = :categoryName")
    suspend fun deleteCategoryAndNotes(categoryName: String)
}