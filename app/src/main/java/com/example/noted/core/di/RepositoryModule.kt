package com.example.noted.core.di

import com.example.noted.core.data.CategoryRepository
import com.example.noted.core.data.NoteRepository
import com.example.noted.core.domain.repository.ICategoryRepository
import com.example.noted.core.domain.repository.INoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideCategoryRepository(categoryRepository: CategoryRepository): ICategoryRepository

    @Binds
    abstract fun provideNoteRepository(noteRepository: NoteRepository): INoteRepository


}