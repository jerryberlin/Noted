package com.example.noted.core.domain.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0,

    val noteTitle: String,

    val noteContent: String,

    val noteDate: String,

    val noteColor: Int = -1,

    val categoryName: String?
) : Parcelable
