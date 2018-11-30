package menegatti.model

import menegatti.data.Note
import menegatti.now
import menegatti.presentation.Repository
import kotlin.random.Random

class NotesRepository : Repository {
    private val notes = mutableListOf<Note>()

    override var refreshListener: List<(List<Note>) -> Unit> = emptyList()

    override suspend fun getNotes() = updateListeners()

    override suspend fun createNote(content: String) {
        Note(generateId(), content, now()).apply { notes.add(this) }
        updateListeners()
    }

    override suspend fun updateNote(noteId: Long, content: String) {
        val noteIndex = notes.indexOfFirst { it.id == noteId }
        if (noteIndex >= 0) {
            notes[noteIndex] = notes[noteIndex].copy(content = content)
        }
        updateListeners()
    }

    override suspend fun deleteNote(noteId: Long) {
        notes.removeAll { it.id == noteId }
        updateListeners()
    }

    private fun updateListeners() {
        refreshListener.forEach { it(notes) }
    }

    private fun generateId() = Random.nextLong(Long.MAX_VALUE)
}