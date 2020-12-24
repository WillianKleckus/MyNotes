package com.kleckus.mynotes.domain

import com.kleckus.mynotes.domain.models.Book
import com.kleckus.mynotes.domain.models.Note

object Constants {
    // Internal constants
    private const val MASTER_BOOK_TITLE = "Master Book"
    private const val BAD_ID = "invalid-book-or-note-id"
    private const val BAD_TITLE = "Error instance"
    private const val BAD_NOTE_CONTENT = "This is a note that appears when something wrong occurs"

    // Public constants
    const val MASTER_BOOK_ID = "master-book"
    const val APP_TAG = "My Notes"
    const val NO_PASSWORD = ""

    val BAD_NOTE = Note(
        id = BAD_ID,
        ownerId = BAD_ID,
        isLocked = false,
        password = NO_PASSWORD,
        title = BAD_TITLE,
        content = BAD_NOTE_CONTENT
    )

    val BAD_BOOK = Book(
        id = BAD_ID,
        isLocked = false,
        password = NO_PASSWORD,
        title = BAD_TITLE,
        noteIds = mutableListOf()
    )

    val INITIAL_BOOK = Book(
        id = MASTER_BOOK_ID,
        isLocked = false,
        password = NO_PASSWORD,
        title = MASTER_BOOK_TITLE,
        noteIds = mutableListOf()
    )
}