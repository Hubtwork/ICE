package com.example.ice.utils

import android.util.Log

object DebugLogger {
    var debugMode = true

    fun log(TAG: String?, `object`: Any) {
        if (debugMode) {
            Log.d(TAG, buildMessage(`object`))
        }
    }

    private fun buildMessage(`object`: Any): String {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()
        sb.append("DebugLogger")
        sb.append(" :: ")
        sb.append(ste.fileName.replace(".java", ""))
        sb.append(" :: ")
        sb.append(`object`.toString())
        return sb.toString()
    }
}