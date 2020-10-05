package com.kleckus.mynotes.system

import android.content.Context
import android.content.Intent
import android.util.Log

private const val MY_NOTE_SYSTEM_TAG = "MyNote Logging"
class Util{
    companion object{
        fun log(logMessage : String){
            Log.i(MY_NOTE_SYSTEM_TAG, logMessage)
        }

        fun navigateTo(from : Context, to: Class<*>, clearHistory : Boolean = false, passId: Int = -1)
        {
            val intent = Intent(from, to)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if(clearHistory)
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if(passId != -1)
                intent.putExtra("id", passId )
            from.startActivity(intent)
        }
    }
}