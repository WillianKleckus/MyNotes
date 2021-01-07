package com.kleckus.mynotes.domain.models

sealed class ModularNote(
    id : String,
    ownerId : String,
    isLocked : Boolean,
    password : String,
    var title : String
) : Item(id,ownerId,isLocked,password) {

    class Text(
        id : String,
        ownerId : String,
        isLocked : Boolean,
        password : String,
        title : String,
        var content : String
        ) : ModularNote(id,ownerId,isLocked,password,title)

    class CheckList(
        id : String,
        ownerId : String,
        isLocked : Boolean,
        password : String,
        title : String,
        checkListItems : List<CheckListItem>
    ) : ModularNote(id,ownerId,isLocked,password,title)
}