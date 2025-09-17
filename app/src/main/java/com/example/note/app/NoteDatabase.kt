package com.example.note.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.dao.NotesDao
import com.example.note.model.NotesData

@Database(version = 1, entities = [NotesData::class])
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NotesDao

}