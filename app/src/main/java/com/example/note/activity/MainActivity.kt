package com.example.note.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.adapter.MyAdapter
import com.example.note.database.NotesDao
import com.example.note.database.NotesDatabase
import com.example.note.databinding.ActivityMainBinding

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

    }

    private fun selectFromDb() = notesDao.getAllNotes()
}