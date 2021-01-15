package com.kleckus.mynotes.domain.models

sealed class ModularItem(var title : String){
    class Text(title : String, var content : String) : ModularItem(title)
    class CheckList(title : String, var checkListItems : List<CheckListItem>) : ModularItem(title)
}