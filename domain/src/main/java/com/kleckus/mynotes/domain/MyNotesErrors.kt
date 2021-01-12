package com.kleckus.mynotes.domain

import java.lang.IllegalArgumentException
import java.lang.NullPointerException

sealed class MyNotesErrors {
    private companion object{
        const val INVALID_TYPE = "Invalid type as argument (Must be inherited from Item type)"
        const val INVALID_OWNER_TYPE_MESSAGE = "Only books can be owners"
        const val BAD_PASSWORD = "Wrong password"
        const val NULL_NOTE_REFERENCE = "Referenced a null note"
    }

    object InvalidArgumentType : IllegalArgumentException(INVALID_TYPE)
    object InvalidOwnerType : IllegalArgumentException(INVALID_OWNER_TYPE_MESSAGE)
    object InvalidPassword : IllegalAccessError(BAD_PASSWORD)
    object NullNoteReference : NullPointerException(BAD_PASSWORD)
}