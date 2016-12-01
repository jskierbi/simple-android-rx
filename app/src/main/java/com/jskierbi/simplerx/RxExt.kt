package com.jskierbi.simplerx

import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers.io

/**
 * Created by jakub on 01/12/16.
 */
fun <T: Any> Observable<T>.ioMain() = subscribeOn(io()).observeOn(mainThread())