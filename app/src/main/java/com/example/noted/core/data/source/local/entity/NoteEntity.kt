package com.example.noted.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "note",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["categoryName"],
        childColumns = ["categoryName"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int,

    val noteTitle: String,

    val noteContent: String,

    val noteDate: String,

    val noteColor: Int = -1,

    val categoryName: String?
)