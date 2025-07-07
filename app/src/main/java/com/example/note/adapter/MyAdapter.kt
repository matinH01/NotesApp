package com.example.note.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.note.activity.AddNotesActivity
import com.example.note.database.NotesData
import com.example.note.database.NotesDatabase
import com.example.note.databinding.RecyclerLayoutBinding

class MyAdapter(
    private val context: Context,
    private val noteData: MutableList<NotesData>
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private val notesDao = NotesDatabase.buildDatabase(context).getNotesDao()

    inner class MyViewHolder(val binding: RecyclerLayoutBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            RecyclerLayoutBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )

    override fun getItemCount() = noteData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.txtTitle.text = noteData[position].title
        holder.binding.txtDescription.text = noteData[position].description
        holder.binding.imgDelete.setOnClickListener {
            notesDao.deleteNotes(noteData[position])
            noteData.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, noteData.size)
        }
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, AddNotesActivity::class.java)
            intent.putExtra("WhichItem", position)
            intent.putExtra("AddOrShow", false)
            context.startActivity(intent)
        }
    }
}