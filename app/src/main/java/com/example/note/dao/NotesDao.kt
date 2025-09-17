package com.example.note.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.note.model.NotesData

@Dao
interface NotesDao {
    @Insert
    suspend fun insertNotes(note: NotesData)

    @Query("SELECT * FROM notes")
     fun getAllNotes(): LiveData<MutableList<NotesData>>

    @Delete
    suspend fun deleteNotes(note: NotesData)
}