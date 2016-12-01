package com.jskierbi.simplerx.platform

/**
 * Created by jakub on 01/12/16.
 */
interface IEventLoop {
    fun post(action: () -> Unit)
}