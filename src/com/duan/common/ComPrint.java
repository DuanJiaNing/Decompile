package com.duan.common;

import org.fusesource.jansi.Ansi;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by DuanJiaNing on 2017/4/20.
 * *
 */
public class ComPrint {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss\t");

    public static void normal(String info) {
        System.out.println(dateFormat.format(Calendar.getInstance().getTime()) + info);
    }

    public static void info(CharSequence logInfo) {
        String str = dateFormat.format(Calendar.getInstance().getTime()) + logInfo;
        System.out.println(ansi().eraseScreen().fg(Ansi.Color.BLUE).a(str).reset());
    }

    public static void error(String errorInfo) {
        PrintStream print = System.err;
        print.println(dateFormat.format(Calendar.getInstance().getTime()) + errorInfo);
    }

}
