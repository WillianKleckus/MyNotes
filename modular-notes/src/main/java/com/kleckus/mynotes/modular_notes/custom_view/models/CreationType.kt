package com.kleckus.mynotes.modular_notes.custom_view.models

internal sealed class CreationType{
    object Text : CreationType()
    object CheckList : CreationType()
}
