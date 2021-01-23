package com.kleckus.mynotes.modular_notes

import android.widget.EditText
import androidx.core.widget.doOnTextChanged

fun EditText.onTextChange(execute : (text : String) -> Unit){
    this.doOnTextChanged { text, _, _, _ ->
        execute(text?.toString() ?: "")
    }
}