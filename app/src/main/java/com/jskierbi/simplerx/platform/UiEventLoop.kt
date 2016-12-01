package com.jskierbi.simplerx.platform

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper

class UiEventLoop : IEventLoop {

    companion object {
        val INSTANCE = UiEventLoop()
    }

    val mHandler = Handler(Looper.getMainLooper())

    override fun post(action: () -> Unit) {
        mHandler.post { action() }
    }
}
