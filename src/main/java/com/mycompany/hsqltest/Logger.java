/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hsqltest;

import java.io.PrintWriter;

/**
 *
 * @author nikolay <nikolay@acs.co.il>
 */
public class Logger {

    private static final PrintWriter writer;

    static {
        writer = new PrintWriter(System.err);
    }

    public static void log(String message, Object... args) {
        System.out.println(String.format(message, args));
    }

    public static void error(String message, Throwable ex) {
        if (message != null && !message.isEmpty()) {
            System.out.println(message);
        }
        if (ex != null) {
            ex.printStackTrace(writer);
        }
    }
}
