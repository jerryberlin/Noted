package com.example.noted.core.domain.model

import androidx.room.PrimaryKey

data class Category(
    @PrimaryKey(autoGenerate = false)
    val categoryName: String
)
