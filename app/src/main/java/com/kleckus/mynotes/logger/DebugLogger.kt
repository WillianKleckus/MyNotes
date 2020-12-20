package com.kleckus.mynotes.logger

import android.util.Log
import com.kleckus.mynotes.domain.Constants.APP_TAG
import com.kleckus.mynotes.domain.services.Logger

class DebugLogger : Logger{
    override fun log(message: String, isImportant: Boolean) {
        Log.i(APP_TAG, message)
    }

    override fun log(message: String, error: Throwable) {
        Log.e(APP_TAG, message, error)
    }
}