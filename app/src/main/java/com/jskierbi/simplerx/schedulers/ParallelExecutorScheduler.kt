package com.jskierbi.simplerx.schedulers

import com.jskierbi.simplerx.platform.ThreadPoolEventLoop
import rx.Scheduler
import rx.Subscription
import rx.functions.Action0
import rx.subscriptions.CompositeSubscription
import rx.subscriptions.Subscriptions
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Created by jakub on 01/12/16.
 */
class ParallelExecutorScheduler() : Scheduler() {

    val poolLoop = ThreadPoolEventLoop.INSTANCE

    override fun createWorker() = ExecutorWorker()

    class ExecutorWorker : Worker() {

        val trackingSubscription = CompositeSubscription()

        override fun schedule(action: Action0?) = schedule(action, 0, null)

        override fun schedule(action: Action0?, delayTime: Long, unit: TimeUnit?) = Subscriptions.empty()

        override fun isUnsubscribed() = trackingSubscription.isUnsubscribed

        override fun unsubscribe() {
            trackingSubscription.unsubscribe()
        }

    }
}