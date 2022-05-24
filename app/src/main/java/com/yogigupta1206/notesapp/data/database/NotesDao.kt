package com.yogigupta1206.notesapp.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yogigupta1206.notesapp.data.model.Note

interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM Note")
    suspend fun deleteAllNotes()

    @Query("SELECT count() FROM Note")
    suspend fun findNotesCount(): Int

    @Query("SELECT * FROM Note")
    suspend fun getAllNotes(): List<Note>

    @Query("UPDATE Note SET title=:title, description=:description where noteId=:id")
    suspend fun updateNote(id: Long, title:String?, description:String ): List<Note>

}