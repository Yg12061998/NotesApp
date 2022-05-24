package com.yogigupta1206.notesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yogigupta1206.notesapp.data.model.Note

@Database(
    entities = [Note::class],
    version = 1
)

abstract class AppDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao
}