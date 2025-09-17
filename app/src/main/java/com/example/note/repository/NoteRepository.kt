package com.example.note.repository

import androidx.lifecycle.LiveData
import com.example.note.dao.NotesDao
import com.example.note.model.NotesData
import javax.inject.Inject

class NoteRepository @Inject constructor(val notesDao: NotesDao) {
     fun fetchNotes(): LiveData<MutableList<NotesData>> = notesDao.getAllNotes()

    suspend fun insertNote(note: NotesData) {
        notesDao.insertNotes(note)
    }

    suspend fun deleteNote(note: NotesData) {
        notesDao.deleteNotes(note)
    }
}