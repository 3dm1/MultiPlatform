package com.example.android

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notes_list.*
import kotlinx.coroutines.Dispatchers
import menegatti.data.Note
import menegatti.model.NotesRepository
import menegatti.presentation.NotesPresenter
import menegatti.presentation.NotesView


class NotesListActivity : AppCompatActivity(), NotesView {

    private val presenter by lazy {
        NotesPresenter(NotesRepository(), Dispatchers.Main)
    }
    private val adapter = NotesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notes_list)

        notesList.layoutManager = LinearLayoutManager(this)
        notesList.adapter = adapter
        notesList.visibility = View.GONE

        adapter.listener = object : NoteOptionListener {
            override fun onEdit(note: Note) {
                presenter.onEditNote(note)
            }

            override fun onDelete(note: Note) {
                presenter.onDeleteNote(note)
            }
        }

        refreshLayout.setOnRefreshListener {
            presenter.onRefreshRequested()
        }

        addNoteButton.setOnClickListener {
            presenter.onAddNote()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.view = this
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.view = null
        presenter.onClear()
    }


    override fun showNotes(notes: List<Note>) {
        notesList.visibility = View.VISIBLE
        adapter.notes = notes
    }

    override fun showEmptyView() {
        notesList.visibility = View.GONE
    }

    override fun openNoteCreation() {
        openEditTextDialog(null) { presenter.onNoteCreated(it) }
    }

    override fun openNoteEditing(id: Long, content: String) {
        openEditTextDialog(content) { presenter.onNoteEdited(id, it) }
    }

    override fun promptDeletionConfirmation(id: Long) {
        AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Deleted notes can't be retrieved")
                .setPositiveButton(android.R.string.ok) { _, _ -> presenter.onNoteDeleted(id) }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }

    private fun openEditTextDialog(content: String?, positiveAction: (String) -> Any?) {
        val input = EditText(this)
        input.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
        input.hint = "Type in your note."
        input.setText(content)

        AlertDialog.Builder(this)
                .setView(input)
                .setPositiveButton(android.R.string.ok) { _, _ -> positiveAction(input.text.toString()) }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }
}