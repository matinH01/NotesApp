package com.example.note.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.R
import com.example.note.adapter.MyAdapter
import com.example.note.databinding.ActivityMainBinding
import com.example.note.model.NotesDao
import com.example.note.model.NotesData
import com.example.note.model.NotesDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesDao: NotesDao
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        notesDao = NotesDatabase.buildDatabase(this).getNotesDao()
        val noteData = selectFromDb()

        setRecyclerViewAdapter(noteData)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("AddOrShow", true)
            startActivity(intent)
        }
    }

    private fun setRecyclerViewAdapter(noteData: MutableList<NotesData>) {
        adapter = MyAdapter(this, noteData)
        binding.layoutManager = LinearLayoutManager(this)
        binding.adapter = adapter
    }

    private fun selectFromDb() = notesDao.getAllNotes()

}