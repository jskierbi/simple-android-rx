package com.jskierbi.simplerx.schedulers

import com.jskierbi.simplerx.platform.IEventLoop
import rx.Scheduler
import rx.Subscription
import rx.functions.Action0
import rx.internal.schedulers.ScheduledAction
import rx.subscriptions.CompositeSubscription
import rx.subscriptions.Subscriptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by jakub on 01/12/16.
 */
class SingleThreadSerialLoopScheduler(val loop: IEventLoop) : Scheduler() {

    override fun createWorker() = Worker(loop)

    class Worker(val loop: IEventLoop) : Scheduler.Worker() {

        val tracking = CompositeSubscription()
        var shutdown = false

        override fun schedule(action: Action0?) = schedule(action, 0, null)

        override fun schedule(action: Action0?, delayTime: Long, unit: TimeUnit?) = when {
            isUnsubscribed -> Subscriptions.unsubscribed()
            else -> ScheduledAction(action).apply {
                tracking.add(this)
                add(Subscriptions.create { tracking.remove(this) })
                loop.post { run() }

                // todo: propagate cancellation to platform side via handle - if needed
                // val handle = loop.post { run() }
                // add(Subscriptions.create { handle.cancel() })

//            exec.submit(this) // For RxCpp, we don't support scheduled actions at this time
//            val future = when {
//                delayTime <= 0 -> exec.submit(this)
//                else -> genericScheduler.schedule({
//                    val scheduledFuture = exec.submit(this)
//                    add(Subscriptions.create { scheduledFuture.cancel(false) })
//                }, delayTime, unit)
//            }
//            add(Subscriptions.create { future.cancel(false) })
            }
        }

        override fun isUnsubscribed() = tracking.isUnsubscribed

        override fun unsubscribe() {
            tracking.unsubscribe()
        }
    }
}