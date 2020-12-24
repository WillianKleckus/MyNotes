package com.kleckus.mynotes.logger

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.kleckus.mynotes.domain.Constants.APP_TAG
import com.kleckus.mynotes.domain.services.Logger

class ProductionLogger : Logger {
    override fun log(message: String) {
        Firebase.analytics.logEvent("$APP_TAG - $message", null)
    }

    override fun log(message: String, error: Throwable) {
        FirebaseCrashlytics.getInstance().apply {
            log("$APP_TAG - $message. Error: ${error.message}")
            recordException(error)
        }
    }
}