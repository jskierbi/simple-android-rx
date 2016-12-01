package com.jskierbi.simplerx.schedulers

import rx.Scheduler
import rx.Subscription
import rx.functions.Action0
import rx.internal.schedulers.NewThreadWorker
import rx.internal.util.RxThreadFactory
import rx.subscriptions.CompositeSubscription
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit

/**
 * This class is using single NewThreadWorker underneath, forwarding all the calls.
 *
 * Created by jakub on 01/12/16.
 */
class ContextAwareScheduler : Scheduler() {

    companion object {
        val INSTANCE = ContextAwareScheduler()
    }

    val worker by lazy { NewThreadWorker(RxThreadFactory("Context Aware Scheduler")) }

    override fun createWorker() = ContextAwareWorker(worker)

    class ContextAwareWorker(val worker: NewThreadWorker) : Worker() {

        val tracking = CompositeSubscription()

        override fun schedule(action: Action0?) = schedule(action, 0, null)
        override fun schedule(action: Action0?, delayTime: Long, unit: TimeUnit?) = when {
            isUnsubscribed -> Subscriptions.unsubscribed()
            else -> worker.scheduleActual(action, delayTime, unit, tracking)
        }

        override fun isUnsubscribed() = tracking.isUnsubscribed
        override fun unsubscribe() {
            tracking.unsubscribe()
        }

    }
}