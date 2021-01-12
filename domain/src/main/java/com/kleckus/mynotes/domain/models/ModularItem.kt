package com.kleckus.mynotes.domain.models

sealed class ModularItem{
    data class Text(var content : String) : ModularItem()
    data class CheckList(var checkListItems : List<CheckListItem>) : ModularItem()
}