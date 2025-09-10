package com.example.note.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.note.state.NoteUiState

class NotesViewModel : ViewModel() {
    private val _uiState = MutableLiveData<NoteUiState>()
    val uiState: LiveData<NoteUiState> = _uiState

    fun setData() {
        if (_uiState.value?.showOrAdd == true) {
            _uiState.value = NoteUiState(topPageText = "یادداشت جدید")
        } else {
            _uiState.value = NoteUiState(showOrAdd = false, topPageText = "نمایش یادداشت")
        }
    }
}