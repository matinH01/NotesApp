package com.example.note.model

class NotesRepository(private val dao: NotesDao) {

    val notes = dao.getAllNotes()

    fun insert(notes: NotesData) {
        dao.insertNotes(notes)
    }

    fun deleteNotes(note: NotesData) {
        dao.deleteNotes(note)
    }
}