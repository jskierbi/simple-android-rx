package com.jskierbi.simplerx

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.jskierbi.simplerx.platform.UiEventLoop
import com.jskierbi.simplerx.schedulers.SingleThreadSerialLoopScheduler
import rx.schedulers.Schedulers.io
import rx.subscriptions.Subscriptions
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private val mVm by lazy { MainActivityVm() }

    private var dataLoadSubscription = Subscriptions.empty()
    private var userActionSubscription = Subscriptions.empty()
    private var dialogSubscription = Subscriptions.empty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById(R.id.btn_call_async).setOnClickListener { view -> subscribeUserAction(Calendar.getInstance()) }
        subscribeLoadData()
    }

    override fun onDestroy() {
        dataLoadSubscription.unsubscribe()
        userActionSubscription.unsubscribe()
        dialogSubscription.unsubscribe() // Hide dialog - prevent window leak
        super.onDestroy()
    }

    private fun subscribeLoadData() {
        dataLoadSubscription.unsubscribe()
        dataLoadSubscription = mVm.data().ioMain().subscribe({ dto ->
            (findViewById(R.id.text_title) as TextView).text = dto.title
            findViewById(R.id.btn_call_async).visibility = View.VISIBLE
        }, { error ->
            // Perform scoped error handling here
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribeUserAction(now: Calendar) {
        (findViewById(R.id.text_manual_async_result) as TextView).text = ""
        userActionSubscription.unsubscribe() // we're not interested in listening to user action anymore
        userActionSubscription = mVm.performUserAction(now)
            .subscribeOn(io())
            .observeOn(SingleThreadSerialLoopScheduler(UiEventLoop.INSTANCE))
            .subscribe({ dto ->
                (findViewById(R.id.text_manual_async_result) as TextView).text = dto.text
            }, { error ->
                // Use closure-captured parameter to perform retry
                Log.e("MainActivity", "Terrible thing happened!", error)
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Operation has failed")
                    .setPositiveButton("retry") { view, which -> subscribeUserAction(now) }
                    .show()
                dialogSubscription.unsubscribe()
                dialogSubscription = Subscriptions.create { dialog.dismiss() }
            })
    }
}
