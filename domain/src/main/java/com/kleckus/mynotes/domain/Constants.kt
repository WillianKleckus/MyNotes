package com.kleckus.mynotes.domain

import com.kleckus.mynotes.domain.models.Item
import com.kleckus.mynotes.domain.models.Item.Book
import com.kleckus.mynotes.domain.models.Item.Note

object Constants {
    // Internal constants
    private const val MASTER_BOOK_TITLE = "Master Book"
    private const val BAD_ID = "invalid-book-or-note-id"
    private const val BAD_TITLE = "Error instance"

    // Public constants
    const val MASTER_BOOK_ID = "master-book"
    const val APP_TAG = "My Notes"
    const val NO_PASSWORD = ""

    val BAD_NOTE = Note(
        id = BAD_ID,
        ownerId = BAD_ID,
        title = BAD_TITLE,
        password = NO_PASSWORD,
        isLocked = false,
        items = listOf()
    )

    val BAD_BOOK = Book(
        id = BAD_ID,
        ownerId = MASTER_BOOK_ID,
        title = BAD_TITLE,
        password = NO_PASSWORD,
        isLocked = false,
        noteIds = mutableListOf()
    )

    val INITIAL_BOOK = Book(
        id = MASTER_BOOK_ID,
        ownerId = MASTER_BOOK_ID,
        title = MASTER_BOOK_TITLE,
        password = NO_PASSWORD,
        isLocked = false,
        noteIds = mutableListOf()
    )
}