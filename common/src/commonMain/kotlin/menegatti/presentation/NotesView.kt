package menegatti.presentation

import menegatti.data.Note

interface NotesView {
    fun showNotes(notes: List<Note>)
    fun showEmptyView()
    fun openNoteCreation()
    fun promptDeletionConfirmation(id: Long)
    fun openNoteEditing(id: Long, content: String)
}