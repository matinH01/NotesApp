package com.example.note.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.model.NotesData
import com.example.note.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    val notes = noteRepository.fetchNotes()

    fun delete(note: NotesData) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }
}