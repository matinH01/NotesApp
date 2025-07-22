package com.example.note.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.ApiService
import com.example.note.R
import com.example.note.adapter.MyAdapter
import com.example.note.database.NotesDao
import com.example.note.database.NotesData
import com.example.note.database.NotesDatabase
import com.example.note.databinding.ActivityMainBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesDao: NotesDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        notesDao = NotesDatabase.buildDatabase(this).getNotesDao()
        val noteData = selectFromDb()

        binding.floatingActionButton.setOnClickListener {
            goToAddNotesActivity()
        }

        setRecyclerViewAdapter(noteData)

        getTimeFromServer()

    }

    private fun goToAddNotesActivity() {
        val intent = Intent(this, AddNotesActivity::class.java)
        intent.putExtra("AddOrShow", true)
        startActivity(intent)
        finish()
    }

    private fun setRecyclerViewAdapter(noteData: MutableList<NotesData>) {
        val adapter = MyAdapter(this, noteData)
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
}