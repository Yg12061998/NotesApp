package com.yogigupta1206.notesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long,
    var title: String?,
    var description: String?
)
