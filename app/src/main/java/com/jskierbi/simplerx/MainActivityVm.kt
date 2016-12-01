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
package com.jskierbi.simplerx

import rx.Observable
import java.util.Calendar

/**
 * Created by jakub on 08/11/16.
 */
class MainActivityVm {
    fun data() = Observable.create<MainActivityDTO> { subscriber ->
        try {
            Thread.sleep(1000)
        } catch (ignore: Throwable) {
        }

        subscriber.onNext(MainActivityDTO("Title from VM"))
        subscriber.onCompleted()
    }

    fun performUserAction(now: Calendar) = Observable.create<MainActivityActionResultDTO> { subscriber ->
        try {
            Thread.sleep(1000)
        } catch (ignore: Throwable) {
        }

        subscriber.onNext(MainActivityActionResultDTO(now))
        subscriber.onCompleted()
    }
}
