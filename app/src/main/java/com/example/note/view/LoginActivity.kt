package com.example.note.view

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.example.note.R
import com.example.note.databinding.ActivityLoginBinding
import com.example.note.view_model.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        requestNotificationPermission()
        binding.apply {
            viewModel = this@LoginActivity.viewModel
            lifecycleOwner = this@LoginActivity
            edtPassword.addTextChangedListener { text ->
                viewModel!!.onPasswordChanged(text.toString())
            }
            edtUsername.addTextChangedListener { text ->
                viewModel!!.onUsernameChanged(text.toString())
            }
        }

        //changes the button text from second execution and after it
        if (preferences.getBoolean("appOpenBefore", false)) {
            binding.btnEnter.text = getString(R.string.enter)
        }
        viewModel.loginUiState.observe(this) { uiState ->
            validateLogin(uiState.usernameText, uiState.passwordText)
        }
    }

    private fun validateLogin(username: String, password: String) {
        when {
            username.isEmpty() || password.isEmpty() -> {
                toast(getString(R.string.enter_pass_user))
            }

            username.contains(' ') || password.contains(' ') -> {
                toast(getString(R.string.do_not_enter_space))
            }

            preferences.getBoolean(KEY_APP_OPEN_BEFORE, false) -> {
                val savedUser = preferences.getString(KEY_USER, "")
                val savedPass = preferences.getString(KEY_PASSWORD, "")
                if (username == savedUser && password == savedPass) {
                    goMainActivity()
                } else {
                    toast(getString(R.string.pass_user_wrong))
                }
            }

            else -> {
                preferences.edit {
                    putString(KEY_USER, username)
                    putString(KEY_PASSWORD, password)
                    putBoolean(KEY_APP_OPEN_BEFORE, true)
                    goMainActivity()
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean -> }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun goMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val KEY_USER = "saveUser"
        private const val KEY_PASSWORD = "savePassword"
        private const val KEY_APP_OPEN_BEFORE = "appOpenBefore"
    }
}