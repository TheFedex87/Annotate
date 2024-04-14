package it.thefedex87.logging.domain

import android.util.Log
import it.thefedex87.logging.data.Logger

class LoggerLog : Logger {
    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }

    override fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }
}