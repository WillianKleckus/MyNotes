package com.kleckus.mynotes.domain

import java.lang.IllegalArgumentException

sealed class MyNotesErrors {
    private companion object{
        const val INVALID_TYPE = "Invalid type as argument (Must be inherited from Item type)"
        const val INVALID_OWNER_TYPE_MESSAGE = "Only books can be owners"
        const val BAD_PASSWORD = "Wrong password"
    }

    object InvalidArgumentType : IllegalArgumentException(INVALID_TYPE)
    object InvalidOwnerType : IllegalArgumentException(INVALID_OWNER_TYPE_MESSAGE)
    object InvalidPassword : IllegalAccessError(BAD_PASSWORD)
}