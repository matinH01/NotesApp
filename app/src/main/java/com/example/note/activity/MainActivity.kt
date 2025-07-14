package com.example.note.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.ApiService
import com.example.note.DataClass
import com.example.note.adapter.MyAdapter
import com.example.note.database.NotesDao
import com.example.note.database.NotesDatabase
import com.example.note.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesDao: NotesDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notesDao = NotesDatabase.buildDatabase(this).getNotesDao()
        val noteData = selectFromDb()

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("AddOrShow", true)
            startActivity(intent)
            finish()
        }

        val adapter = MyAdapter(this, noteData)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        getTimeFromServer()

    }

    private fun selectFromDb() = notesDao.getAllNotes()

    private fun getTimeFromServer() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://brsapi.ir/")
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getData()

        call.enqueue(object : Callback<DataClass> {
            override fun onResponse(call: Call<DataClass>, response: Response<DataClass>) {
                if (response.body() != null && response.isSuccessful) {
                    val date = response.body()!!.gold[0].date
                    binding.textView2.text = date
                }
            }

            override fun onFailure(call: Call<DataClass>, t: Throwable) {
                Log.e("API_ERROR", t.message ?: "Unknown error")
            }
        })
    }
}