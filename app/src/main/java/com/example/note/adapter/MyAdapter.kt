package com.example.note.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.note.databinding.RecyclerLayoutBinding
import com.example.note.model.NotesData
import com.example.note.view.AddNotesActivity

class MyAdapter(
    private val context: Context,
    private val onDeleteClick: (NotesData) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var noteData = mutableListOf<NotesData>()
    fun submitList(list: MutableList<NotesData>) {
        noteData = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: RecyclerLayoutBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            RecyclerLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = noteData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = noteData[position]
        holder.binding.txtTitle.text = note.title
        holder.binding.txtDescription.text = note.description
        holder.binding.imgDelete.setOnClickListener {
            onDeleteClick(note)
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