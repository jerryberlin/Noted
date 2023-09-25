package com.example.noted.di

import com.example.noted.core.domain.usecase.CategoryInteractor
import com.example.noted.core.domain.usecase.CategoryUseCase
import com.example.noted.core.domain.usecase.NoteInteractor
import com.example.noted.core.domain.usecase.NoteUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideCategoryUseCase(categoryInteractor: CategoryInteractor): CategoryUseCase

    @Binds
    @Singleton
    abstract fun provideNoteUseCase(noteInteractor: NoteInteractor): NoteUseCase
}