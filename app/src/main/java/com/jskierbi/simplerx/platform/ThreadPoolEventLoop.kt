package com.jskierbi.simplerx.platform

import java.util.concurrent.Executors

/**
 * Created by jakub on 01/12/16.
 */
class ThreadPoolEventLoop : IEventLoop {

    companion object {
        val INSTANCE = ThreadPoolEventLoop()
    }

    val poolExecutor = Executors.newFixedThreadPool(4)

    override fun post(action: () -> Unit) {
        poolExecutor.execute(action)
    }

}