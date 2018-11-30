package menegatti.presentation

import menegatti.data.Note

interface Repository {
    var refreshListener: List<(List<Note>) -> Unit>
    suspend fun getNotes()
    suspend fun createNote(content : String)
    suspend fun updateNote(noteId: Long, content:String)
    suspend fun deleteNote(noteId : Long)
}