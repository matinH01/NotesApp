package com.example.note.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.note.state.AddNoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNotesViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableLiveData<AddNoteUiState>()
    val uiState: LiveData<AddNoteUiState> = _uiState

    fun setData() {
        if (_uiState.value?.showOrAdd == true) {
            _uiState.value = AddNoteUiState(topPageText = "یادداشت جدید")
        } else {
            _uiState.value = AddNoteUiState(showOrAdd = false, topPageText = "نمایش یادداشت")
        }
    }
}