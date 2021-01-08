package com.kleckus.mynotes.domain.models

sealed class Item{
    data class Text(var content : String) : Item()
    data class CheckList(var checkListItems : List<CheckListItem>) : Item()
}