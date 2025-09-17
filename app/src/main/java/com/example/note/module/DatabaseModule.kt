package com.example.note.module

import android.content.Context
import androidx.room.Room
import com.example.note.app.NoteDatabase
import com.example.note.dao.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_database"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideUserDao(db: NoteDatabase): NotesDao {
        return db.noteDao()
    }

}