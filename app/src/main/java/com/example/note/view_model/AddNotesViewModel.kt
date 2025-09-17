package com.example.note.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.model.NotesData
import com.example.note.repository.NoteRepository
import com.example.note.state.AddNoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private var whichItem: Int? = null
    private val _uiState = MutableLiveData<AddNoteUiState>()
    val uiState: LiveData<AddNoteUiState> = _uiState

    fun setData() {
        viewModelScope.launch {
            if (_uiState.value?.showOrAdd == true) {
                _uiState.value = AddNoteUiState()
                if (_uiState.value!!.title.isNotEmpty() && _uiState.value!!.description.isNotEmpty()) {
                    noteRepository.insertNote(
                        NotesData(
                            title = _uiState.value!!.title,
                            description = _uiState.value!!.description
                        )
                    )
                }
            } else {
                _uiState.value = AddNoteUiState(showOrAdd = false, textEnable = false)
                whichItem = _uiState.value!!.whichItem
                if (whichItem != -1) {
                    _uiState.value?.title =
                        noteRepository.fetchNotes().value?.get(whichItem!!)?.title.toString()
                    _uiState.value?.description =
                        noteRepository.fetchNotes().value?.get(whichItem!!)?.description.toString()
                }
            }
        }
    }
}