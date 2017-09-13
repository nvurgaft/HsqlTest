/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hsqltest;

/**
 *
 * @author nikolay <nikolay@acs.co.il>
 */
public class Logger {
    public static void log(String message, Object... args) {
        System.out.println(String.format(message, args));
    }
}
