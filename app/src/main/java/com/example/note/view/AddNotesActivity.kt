package com.example.note.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.example.note.R
import com.example.note.databinding.ActivityAddNotesBinding
import com.example.note.model.NotesDao
import com.example.note.model.NotesData
import com.example.note.model.NotesDatabase
import com.example.note.viewModel.NotesViewModel

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var notesDao: NotesDao
    private val viewModel = NotesViewModel()
    private val channelId = "notification"
    private val channelName = "General Notifications"
    private var addOrShow = true
    private var whichItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_notes)
        init()
    }

    private fun init() {
        onBackButtonPressed {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            false
        }
        notesDao = NotesDatabase.buildDatabase(this).getNotesDao()
        addOrShow = intent.extras!!.getBoolean("AddOrShow")
        whichItem = intent.extras!!.getInt("WhichItem")

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.uiState.observe(this) { noteUiState ->
            noteUiState.showOrAdd = addOrShow
            noteUiState.title = binding.edtTitle.text.toString()
            noteUiState.description = binding.edtDescription.text.toString()
            if (noteUiState.showOrAdd) {
                if (noteUiState.title.isNotEmpty() && noteUiState.description.isNotEmpty()) {
                    insertToDb(
                        NotesData(
                            title = noteUiState.title,
                            description = noteUiState.description
                        )
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    showNotification()
                    finish()
                } else {
                    Toast.makeText(this, "لطفا عنوان و شرح را وارد کنید!!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                noteUiState.title = notesDao.getAllNotes()[whichItem].title
                noteUiState.description = notesDao.getAllNotes()[whichItem].description
                binding.btnSave.visibility = View.INVISIBLE
            }
        }

    }


    private fun insertToDb(notesData: NotesData) {
        notesDao.insertNotes(notesData)
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(this, channelId)
        notification.setSmallIcon(R.drawable.note_icon)
        notification.setContentTitle("یادداشت")
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