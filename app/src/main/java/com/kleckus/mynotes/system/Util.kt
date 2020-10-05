package com.kleckus.mynotes.system

import android.util.Log

private const val MY_NOTE_SYSTEM_TAG = "MyNote Logging"
class Util{
    companion object{
        fun log(logMessage : String){
            Log.i(MY_NOTE_SYSTEM_TAG, logMessage)
        }
    }
}