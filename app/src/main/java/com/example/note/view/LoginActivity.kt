package com.example.note.view

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.note.R
import com.example.note.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        requestNotificationPermission()
        intent = Intent(this, MainActivity::class.java)
        preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()     
        editor.apply()

        if (preferences.getBoolean("appOpenBefore", false)) {
            binding.btnEnter.text = getString(R.string.enter)
        }

        binding.btnEnter.setOnClickListener {
            val userName = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()

            // checks username edit text and password edit text is not empty!
            if (userName.isEmpty() || password.isEmpty()) {
                snackBar(getString(R.string.enter_pass_user))
            } else if (userName.contains(' ') || password.contains(' ')) {
                snackBar(getString(R.string.do_not_enter_space))
            } else {

                val appOpenBefore = preferences.getBoolean("appOpenBefore", false)

                if (appOpenBefore) {
                    if (userName == preferences.getString("saveUser", "")
                        && password == preferences.getString("savePassword", "")
                    ) {
                        goMainActivity(userName)
                    } else {
                        snackBar(getString(R.string.pass_user_wrong))
                    }
                } else {
                    editor.putString("saveUser", userName).apply()
                    editor.putString("savePassword", password).apply()
                    editor.putBoolean("appOpenBefore", true).apply()
                    // intents from this activity to MainActivity
                    goMainActivity(userName)
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

    private fun goMainActivity(userName: String) {
        intent.putExtra("userName", userName)
        startActivity(intent)
        finish()
    }

    private fun snackBar(text: String) {
        val snackBar = Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(
                Color.GRAY
            )
        val params = snackBar.view.layoutParams as (FrameLayout.LayoutParams)
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.BOTTOM or Gravity.CENTER // Bottom centre
        snackBar.view.background = ContextCompat.getDrawable(this, R.drawable.shape_round)
        snackBar.show()
    }
}


