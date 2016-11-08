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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jakub on 08/11/16.
 */
public class MainActivityActionResultDTO {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public final String text;

    public MainActivityActionResultDTO(Calendar date) {
        text = "Clicked at: " + DATE_FORMAT.format(date.getTime());
    }
}
