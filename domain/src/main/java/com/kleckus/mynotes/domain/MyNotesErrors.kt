package com.kleckus.mynotes.domain

import java.lang.IllegalArgumentException

sealed class MyNotesErrors {
    private companion object{
        const val BOOK_OR_NOTE_MESSAGE = "Argument must be a book or a note"
        const val INVALID_OWNER_TYPE_MESSAGE = "Only books can be owners"
    }

    object NonNoteOrBookArgument : IllegalArgumentException(BOOK_OR_NOTE_MESSAGE)
    object InvalidOwnerType : IllegalArgumentException(INVALID_OWNER_TYPE_MESSAGE)
}