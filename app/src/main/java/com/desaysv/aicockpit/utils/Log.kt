package com.desaysv.aicockpit.utils


import android.content.Intent
import android.os.Bundle
import android.util.Log as AndroidLog

object Log {

    private const val VERBOSE = 2
    private const val DEBUG = 3
    private const val INFO = 4
    private const val WARN = 5
    private const val ERROR = 6

    var debugEnabled: Boolean = true
    var moduleName: String = "ai座舱"
    var isLongSupport: Boolean = false

    fun v(msg: String, tag: String = "nono") {
        if (debugEnabled) logContent(VERBOSE, tag, msg)
    }

    fun d(msg: String, tag: String = "nono") {
        if (debugEnabled) logContent(DEBUG, tag, msg)
    }

    fun i(msg: String, tag: String = "nono") {
        if (debugEnabled) logContent(INFO, tag, msg)
    }

    fun w(msg: String, tag: String = "nono") {
        if (debugEnabled) logContent(WARN, tag, msg)
    }

    fun e(msg: String, tag: String = "nono") {
        if (debugEnabled) logContent(ERROR, tag, msg)
    }

    private fun logContent(type: Int, tag: String, content: String?) {
        if (content.isNullOrEmpty()) return

        val stackTrace = Thread.currentThread().stackTrace
        val hasMoreThanTwo = stackTrace.size >= 6

        val caller1 = stackTrace.getOrNull(4)?.let {
            "${it.fileName} ${it.lineNumber} "
        } ?: ""

        val caller2 = stackTrace.getOrNull(5)?.let {
            "${it.fileName} ${it.lineNumber}->"
        } ?: ""

        val callerInfo = if (hasMoreThanTwo) caller2 + caller1 else caller1
        val finalContent = callerInfo + content

        if (!isLongSupport || finalContent.length < 4000) {
            logPrint(type, tag, finalContent)
        } else {
            val bytes = finalContent.toByteArray()
            val length = bytes.size
            var i = 0
            while (i < length) {
                val count = minOf(length - i, 4000)
                val msg = String(bytes, i, count)
                logPrint(type, tag, msg)
                i += 4000
            }
        }
    }

    private fun logPrint(type: Int, tag: String, content: String) {
        val logMsg = "[ $tag ] : $content"
        when (type) {
            VERBOSE -> AndroidLog.v(moduleName, logMsg)
            DEBUG -> AndroidLog.d(moduleName, logMsg)
            INFO -> AndroidLog.i(moduleName, logMsg)
            WARN -> AndroidLog.w(moduleName, logMsg)
            ERROR -> AndroidLog.e(moduleName, logMsg)
        }
    }

    fun printAllBundleExtra(intent: Intent?) {
        intent?.extras?.let { bundle ->
            for (key in bundle.keySet()) {
                val value = bundle.get(key)
                d("key: $key, val: $value", tag = "debugman")
            }
        }
    }
}
