package com.example.note.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.example.note.R
import com.example.note.model.NotesDao
import com.example.note.model.NotesData
import com.example.note.model.NotesDatabase
import com.example.note.databinding.ActivityAddNotesBinding

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var notesDao: NotesDao
    private lateinit var btnSave: Button
    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
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
        bindViews()
        onBackButtonPressed {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            false
        }

        notesDao = NotesDatabase.buildDatabase(this).getNotesDao()
        addOrShow = intent.extras!!.getBoolean("AddOrShow")
        whichItem = intent.extras!!.getInt("WhichItem")

        if (addOrShow) {
            btnSave.setOnClickListener {
                saveNotes()
            }
        } else {
            showNotes()
        }
    }


    private fun bindViews() {
        btnSave = binding.btnSave
        edtTitle = binding.edtTitle
        edtDescription = binding.edtDescription
    }

    private fun saveNotes() {
        val title = edtTitle.text.toString()
        val description = edtDescription.text.toString()
        if (title.isNotEmpty() && description.isNotEmpty()) {
            insertToDb(NotesData(title = title, description = description))
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            showNotification("یادداشت", "یادداشت با موفقیت ذخیره شد!!")
            finish()
        } else {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showNotes() {
        binding.textView.text = "نمایش یادداشت"
        edtTitle.isEnabled = false
        edtDescription.isEnabled = false
        edtTitle.setText(notesDao.getAllNotes()[whichItem].title)
        edtDescription.setText(notesDao.getAllNotes()[whichItem].description)
        btnSave.visibility = View.INVISIBLE
    }


    private fun insertToDb(notesData: NotesData) {
        notesDao.insertNotes(notesData)
    }


    private fun showNotification(title: String, text: String) {
        val notification = NotificationCompat.Builder(this, channelId)
        notification.setSmallIcon(R.drawable.note_icon)
        notification.setContentTitle(title)
        notification.setContentText(text)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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