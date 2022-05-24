package com.yogigupta1206.notesapp.repository

import android.util.Log
import com.yogigupta1206.notesapp.data.database.NotesDao
import com.yogigupta1206.notesapp.data.model.Note
import com.yogigupta1206.notesapp.utils.getCurrentEpochTime
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val dao: NotesDao,
) {

    suspend fun getNotesData(): MutableList<Note> {
        val data = mutableListOf<Note>()
        if(isDataAvailable()){
            data.addAll(dao.getAllNotes())
        }
        return data
    }

    private suspend fun isDataAvailable(): Boolean {
        if(dao.findNotesCount() > 0)
            return true
        return false
    }

    suspend fun insertData(note: Note): Note {
        note.timeStamp = getCurrentEpochTime()
        dao.insertNote(note)
        return dao.getLastEntry()
    }

    suspend fun deleteAllNotes(){
        dao.deleteAllNotes()
    }

    suspend fun deleteNote(note: Note){
        dao.deleteNote(note)
    }

    suspend fun updateNote(note:Note): Note {
        note.timeStamp = getCurrentEpochTime()
        dao.updateNote(note.noteId, note.title, note.description, note.timeStamp)
        return dao.getLastEntry()
    }

}