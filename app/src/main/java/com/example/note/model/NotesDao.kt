package com.example.note.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Insert
    fun insertNotes(note: NotesData)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): MutableList<NotesData>

    @Delete
    fun deleteNotes(note: NotesData)
}