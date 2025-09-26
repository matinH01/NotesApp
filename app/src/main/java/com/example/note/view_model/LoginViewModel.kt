package com.example.note.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.note.state.LoginUiState

class LoginViewModel : ViewModel() {
    private val _loginUiState = MutableLiveData<LoginUiState>()
    val loginUiState: LiveData<LoginUiState> = _loginUiState
    private var username: String = ""
    private var password: String = ""

    // for updating fields
    fun onUsernameChanged(user: String) {
        username = user
    }

    fun onPasswordChanged(pass: String) {
        password = pass
    }

    fun checkData() {
        _loginUiState.value = LoginUiState(username, password)
    }

}