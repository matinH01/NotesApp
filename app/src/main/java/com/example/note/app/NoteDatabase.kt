package com.example.note.app

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.stateholosen.dao.UserDao
import com.example.stateholosen.model.User

@Database(version = 5, entities = [User::class])
abstract class NoteDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

}