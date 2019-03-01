package adevlibs.log;

import android.util.Log;

/**
 * The log helper
 * 
 * @author wangfan
 * 
 * @date 2012/12/18
 */
public class CommonLog {
    /**
     * The flag whether the debug log is output or not.
     */
    private static boolean s_bDebug = false;

    /**
     * @see android.util.Log#e(String, String)
     */
    public static int e(String msg) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.e(stackTrace.getFileName(), msg);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#e(String, String, Throwable)
     */
    public static int e(String msg, Throwable tr) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.e(stackTrace.getFileName(), msg, tr);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#w(String, String)
     */
    public static int w(String msg) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.w(stackTrace.getFileName(), msg);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#w(String, String, Throwable)
     */
    public static int w(String msg, Throwable tr) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.w(stackTrace.getFileName(), msg, tr);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#w(String, Throwable)
     */
    public static int w(Throwable tr) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            return Log.w(stackTrace.getFileName(), tr);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#d(String, String)
     */
    public static int d(String msg) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.d(stackTrace.getFileName(), msg);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#d(String, String)
     */
    public static int d(String tag, String msg) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.d(tag, msg);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#d(String, String, Throwable)
     */
    public static int d(String msg, Throwable tr) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.d(stackTrace.getFileName(), msg, tr);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#i(String, String)
     */
    public static int i(String msg) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.i(stackTrace.getFileName(), msg);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#i(String, String, Throwable)
     */
    public static int i(String msg, Throwable tr) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.i(stackTrace.getFileName(), msg, tr);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#v(String, String)
     */
    public static int v(String msg) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.v(stackTrace.getFileName(), msg);
        } else {
            return 0;
        }
    }

    /**
     * @see android.util.Log#v(String, String, Throwable)
     */
    public static int v(String msg, Throwable tr) {
        if (s_bDebug) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") "
                    + stackTrace.getMethodName();
            msg = fileInfo + ": " + msg;
            return Log.v(stackTrace.getFileName(), msg, tr);
        } else {
            return 0;
        }
    }

    public static boolean isDebug() {
        return s_bDebug;
    }

    /**
     * Turn off log
     */
    public static void turnOff() {
        s_bDebug = false;
    }

    /**
     * Turn on log
     */
    public static void turnOn() {
        s_bDebug = true;
    }
}
