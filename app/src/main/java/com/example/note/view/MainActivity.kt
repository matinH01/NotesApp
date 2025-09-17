package com.example.note.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.R
import com.example.note.adapter.MyAdapter
import com.example.note.dao.NotesDao
import com.example.note.databinding.ActivityMainBinding
import com.example.note.view_model.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesDao: NotesDao
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        notesDao = NotesDatabase.buildDatabase(this).getNotesDao()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("AddOrShow", true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setData()
        viewModel.mainUiState.observe(this) { mainUiState ->
            mainUiState.adapter = MyAdapter(this, selectFromDb())
            mainUiState.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun selectFromDb() = notesDao.getAllNotes()
}