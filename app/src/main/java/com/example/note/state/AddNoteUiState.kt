package com.example.note.state

data class AddNoteUiState(
    var title: String = "",
    var description: String = "",
    var showOrAdd: Boolean = true,
    var textEnable: Boolean = false,
    var whichItem: Int = -1
)
