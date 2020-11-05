package com.md.sda.aspects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class SDAAspect {

    /**
     * Format date - Days / Months / 4 digit year / hours / minutes / seconds / milli seconds / nano seconds
     */
    public DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS:SSSS");

}
