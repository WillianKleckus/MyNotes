package com.kleckus.mynotes.modular_notes.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.kleckus.mynotes.modular_notes.R

class ModularNoteView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_modular_note_view, null)
    }

    fun addText(){

    }

    fun addCheckList(){

    }

}