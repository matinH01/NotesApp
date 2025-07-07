package com.example.note.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.R
import com.example.note.adapter.MyAdapter
import com.example.note.database.NotesDao
import com.example.note.database.NotesDatabase
import com.example.note.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

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

        snackBar()
    }

    private fun selectFromDb() = notesDao.getAllNotes()
    private fun snackBar() {
        val snackBar =
            Snackbar.make(binding.root, getString(R.string.enter_message), Snackbar.LENGTH_SHORT)
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