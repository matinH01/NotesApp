package com.example.note.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.example.note.R
import com.example.note.databinding.ActivityAddNotesBinding
import com.example.note.view_model.AddNotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    private val channelId = "notification"
    private val channelName = "General Notifications"
    private val viewModel: AddNotesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_notes)
        init()
    }

    private fun init() {
        onBackButtonPressed {
            finish()
            false
        }
        val addOrShow = intent.extras!!.getBoolean("AddOrShow")
        val whichItem = intent.extras!!.getInt("WhichItem")

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.uiState.observe(this) { noteUiState ->
            noteUiState.textEnable = true
            noteUiState.showOrAdd = addOrShow
            noteUiState.title = binding.edtTitle.text.toString()
            noteUiState.description = binding.edtDescription.text.toString()
            if (noteUiState.showOrAdd) {
                if (noteUiState.title.isNotEmpty() && noteUiState.description.isNotEmpty()) {
                    showNotification()
                    finish()
                } else {
                    Toast.makeText(this, "لطفا عنوان و شرح را وارد کنید!!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                noteUiState.textEnable = false
                noteUiState.whichItem = whichItem
                binding.btnSave.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setData()
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(this, channelId)
        notification.setSmallIcon(R.drawable.note_icon)
        notification.setContentTitle("Note")
        notification.setContentText("یادداشت با موفقیت ذخیره شد!!")

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createChannel(notificationManager)
        notificationManager.notify(0, notification.build())
    }

    private fun createChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Channel for general app notifications"
                    lightColor = Color.RED
                    enableVibration(true)
                }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun onBackButtonPressed(callback: (() -> Boolean)) {
        (this as? FragmentActivity)?.onBackPressedDispatcher?.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!callback()) {
                        remove()
                        performBackPress()
                    }
                }
            })
    }

    fun performBackPress() {
        (this as? FragmentActivity)?.onBackPressedDispatcher?.onBackPressed()
    }
}