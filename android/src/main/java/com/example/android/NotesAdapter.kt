package com.example.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_note_tile.view.*
import menegatti.data.Note

class NotesAdapter : RecyclerView.Adapter<NoteViewHolder>() {

    var notes: List<Note> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: NoteOptionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_note_tile, parent, false)
        return NoteViewHolder(view, listener)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }
}

class NoteViewHolder(itemView: View, private val listener: NoteOptionListener?) : RecyclerView.ViewHolder(itemView) {

    lateinit var note: Note

    init {
        itemView.popUpAnchor.setOnClickListener {
            val menu = PopupMenu(itemView.context, itemView.popUpAnchor)
            menu.inflate(R.menu.menu_note_options)
            menu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.actionEdit -> listener?.onEdit(note)
                    R.id.actionDelete -> listener?.onDelete(note)
                }
                true
            }
            menu.show()
        }
    }

    fun bind(note: Note) {
        this.note = note
        itemView.noteContent.text = note.content
    }
}

interface NoteOptionListener {
    fun onEdit(note: Note)
    fun onDelete(note: Note)
}