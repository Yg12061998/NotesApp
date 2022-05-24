package com.yogigupta1206.notesapp.data.database

import androidx.room.*
import com.yogigupta1206.notesapp.data.model.Note

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM Note")
    suspend fun deleteAllNotes()

    @Query("SELECT count() FROM Note")
    suspend fun findNotesCount(): Int

    @Query("SELECT * FROM Note ORDER BY noteId DESC")
    suspend fun getAllNotes(): List<Note>

    @Query("UPDATE Note SET title=:title, description=:description where noteId=:id")
    suspend fun updateNote(id: Int, title:String?, description:String?): List<Note>

    @Delete
    suspend fun deleteNote(note:Note)

    @Query("SELECT * FROM Note ORDER BY noteId DESC LIMIT 1")
    suspend fun getLastEntry(): Note

}