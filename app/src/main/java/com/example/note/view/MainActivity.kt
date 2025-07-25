package com.example.note.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.ApiService
import com.example.note.R
import com.example.note.adapter.MyAdapter
import com.example.note.databinding.ActivityMainBinding
import com.example.note.model.NotesDao
import com.example.note.model.NotesData
import com.example.note.model.NotesDatabase
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesDao: NotesDao
    private lateinit var adapter: MyAdapter
    private val channelId = "notification"
    private val channelName = "General Notifications"
    private var addOrShow = true
    private var whichItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        addOrShow = intent.extras!!.getBoolean("Show")
        whichItem = intent.extras!!.getInt("WhichItem")

        notesDao = NotesDatabase.buildDatabase(this).getNotesDao()
        val noteData = selectFromDb()

        setRecyclerViewAdapter(noteData)

        if (addOrShow) {
            showNotes()
        }

        binding.floatingActionButton.setOnClickListener {
            showAddNoteLayout()
        }

        getTimeFromServer()

    }

    private fun showAddNoteLayout() {
        binding.edtTitle.setText("")
        binding.edtDescription.setText("")
        binding.layoutMain.visibility = View.INVISIBLE
        binding.layoutAddNotes.visibility = View.VISIBLE
        binding.btnSave.setOnClickListener {
            saveNotes()
        }
    }

    private fun setRecyclerViewAdapter(noteData: MutableList<NotesData>) {
        adapter = MyAdapter(this, noteData)
        binding.layoutManager = LinearLayoutManager(this)
        binding.adapter = adapter
    }

    private fun selectFromDb() = notesDao.getAllNotes()

    private fun getTimeFromServer() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://brsapi.ir/")
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getData()

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body() != null && response.isSuccessful) {
                    val jsonObject = JSONObject(response.body()!!)
                    binding.textDate =
                        "${jsonObject.getJSONArray("gold").getJSONObject(0).get("date")}"
                    binding.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_ERROR", t.message ?: "Unknown error")
            }
        })
    }

    private fun showNotes() {
        binding.layoutMain.visibility = View.INVISIBLE
        binding.layoutAddNotes.visibility = View.VISIBLE
        binding.textView.text = "نمایش یادداشت"
        binding.edtTitle.isEnabled = false
        binding.edtDescription.isEnabled = false
        binding.edtTitle.setText(notesDao.getAllNotes()[whichItem].title)
        binding.edtDescription.setText(notesDao.getAllNotes()[whichItem].description)
        binding.btnSave.visibility = View.INVISIBLE
    }

    private fun saveNotes() {
        val title = binding.edtTitle.text.toString()
        val description = binding.edtDescription.text.toString()
        if (title.isNotEmpty() && description.isNotEmpty()) {
            insertToDb(NotesData(title = title, description = description))
            showNotification("یادداشت", "یادداشت با موفقیت ذخیره شد!!")
            binding.layoutAddNotes.visibility = View.INVISIBLE
            binding.layoutMain.visibility = View.VISIBLE
            adapter = MyAdapter(this, selectFromDb())
            binding.adapter = adapter
        } else {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT)
                .show()
        }
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
}