/*
 * (c) Neofonie Mobile GmbH (2016)
 *
 * This computer program is the sole property of Neofonie Mobile GmbH (http://mobile.neofonie.de)
 * and is protected under the German Copyright Act (paragraph 69a UrhG).
 *
 * All rights are reserved. Making copies, duplicating, modifying, using or distributing
 * this computer program in any form, without prior written consent of Neofonie Mobile GmbH, is prohibited.
 * Violation of copyright is punishable under the German Copyright Act (paragraph 106 UrhG).
 *
 * Removing this copyright statement is also a violation.
 */
package com.jskierbi.simplerx;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.Calendar;

/**
 * Created by jakub on 08/11/16.
 */
public class MainActivityVm {

    private boolean flgLastFailed = true;

    public static MainActivityVm create() {
        return new MainActivityVm();
    }

    public Observable<MainActivityDTO> getData() {
        return Observable.<MainActivityDTO>create(new Observable.OnSubscribe<MainActivityDTO>() {
            @Override
            public void call(Subscriber<? super MainActivityDTO> subscriber) {
                try { Thread.sleep(1000); } catch (Throwable ignore) {}
                subscriber.onNext(new MainActivityDTO("Title from VM"));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MainActivityActionResultDTO> performUserAction(Calendar now) {
        return Observable.<MainActivityActionResultDTO>create(new Observable.OnSubscribe<MainActivityActionResultDTO>() {
            @Override
            public void call(Subscriber<? super MainActivityActionResultDTO> subscriber) {
                try { Thread.sleep(1000); } catch (Throwable ignore) {}
                if (flgLastFailed) {
                    subscriber.onNext(new MainActivityActionResultDTO(now));
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new RuntimeException("Operation failed"));
                }
                flgLastFailed = !flgLastFailed;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
