package com.pir.util;

/**
 * Created by pritesh on 12/14/13.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: si
 * Date: 6/16/12
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ErrorUtils {


    /**
     * Return a stack trace of a given exception;
     *
     * @param e
     * @return a string of stack trace.
     */
    public static String getStackTraceString(Throwable e) {
        /*StringBuffer sb = new StringBuffer();

        if(ex == null) {
            return "";
        }
        sb.append(getStackTraceString(ex.getStackTrace(), 0));
        return sb.toString();*/


        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().toString()+"\n");
        // Get the full stacktrace from exception and append to the string
        // Thanks to ripper234 (ref: http://stackoverflow.com/questions/1292858/getting-full-string-stack-trace-including-inner-exception)
        sb.append(joinStackTrace(e));
        return sb.toString();
    }

    public static String getStackTraceString() {
        // remove the first element since it'll be the call to this method (start from index 2)
        return getStackTraceString(Thread.currentThread().getStackTrace(), 2);
    }

    public static String getStackTraceString(StackTraceElement[] stackTraceElements, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int x = startIndex; x < stackTraceElements.length; x++) {
            sb.append(stackTraceElements[x].getClassName());
            sb.append(".");
            sb.append(stackTraceElements[x].getMethodName());
            sb.append(" (");
            sb.append(stackTraceElements[x].getFileName());
            sb.append(":");
            sb.append(stackTraceElements[x].getLineNumber());
            sb.append(")\n");
        }
        return sb.toString();
    }

    public static String joinStackTrace(Throwable e) {
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            joinStackTrace(e, writer);
            return writer.toString();
        }
        finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {
                    // ignore
                }
        }
    }

    public static void joinStackTrace(Throwable e, StringWriter writer) {
        PrintWriter printer = null;
        try {
            printer = new PrintWriter(writer);

            while (e != null) {

                printer.println(e);
                StackTraceElement[] trace = e.getStackTrace();
                for (int i = 0; i < trace.length; i++)
                    printer.println("\tat " + trace[i]);

                e = e.getCause();
                if (e != null)
                    printer.println("Caused by:\r\n");
            }
        }
        finally {
            if (printer != null)
                printer.close();
        }
    }

}

