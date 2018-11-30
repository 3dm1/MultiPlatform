package menegatti.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import menegatti.data.Note
import kotlin.coroutines.CoroutineContext

class NotesPresenter(private val repository: Repository,
                     private val ui: CoroutineDispatcher) : CoroutineScope {

    var view: NotesView? = null
    private val refreshListener = this::notesListener

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + ui

    fun onStart() {
        repository.refreshListener += refreshListener
        onRefreshRequested()
    }

    fun onNoteCreated(content: String) = launch { repository.createNote(content) }

    fun onNoteEdited(noteId: Long, content: String) = launch { repository.updateNote(noteId, content) }

    fun onNoteDeleted(noteId: Long) = launch { repository.deleteNote(noteId) }

    fun onRefreshRequested() = launch { repository.getNotes() }

    fun onAddNote() {
        view?.openNoteCreation()
    }

    fun onEditNote(note: Note) {
        view?.openNoteEditing(note.id, note.content)
    }

    fun onDeleteNote(note: Note) {
        view?.promptDeletionConfirmation(note.id)
    }

    private fun notesListener(notes: List<Note>) {
        if (notes.isEmpty()) {
            view?.showEmptyView()
        } else {
            view?.showNotes(notes)
        }
    }

    fun onClear() {
        job.cancel()
        repository.refreshListener -= refreshListener
    }
}