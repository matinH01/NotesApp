package com.example.note.state

data class NoteUiState(
    var title: String = "",
    var description: String = "",
    var showOrAdd: Boolean = true,
    var topPageText: String = "یادداشت جدید"
)
