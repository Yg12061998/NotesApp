package com.yogigupta1206.notesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0,
    var title: String?,
    var description: String?,
    var timeStamp: Long = 0
) {
    constructor() : this(0,"","")
}
