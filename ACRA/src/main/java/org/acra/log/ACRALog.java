package org.acra.log;

/**
 * Responsible for providing ACRA classes with a platform neutral way of logging.
 * <p>
 *     One reason for using this mechanism is to allow ACRA classes to use a logging system,
 *     but be able to execute in a test environment outside of an Android JVM.
 * </p>
 * @author William Ferguson
 * @since 4.3.0
 */
public interface ACRALog {
    public int v(String tag, String msg);
    public int v(String tag, String msg, Throwable tr);
    public int d(String tag, String msg);
    public int d(String tag, String msg, Throwable tr);
    public int i(String tag, String msg);
    public int i(String tag, String msg, Throwable tr);
    public int w(String tag, String msg);
    public int w(String tag, String msg, Throwable tr);
    //public native  boolean isLoggable(java.lang.String tag, int level);
    public int w(String tag, Throwable tr);
    public int e(String tag, String msg);
    public int e(String tag, String msg, Throwable tr);
    public String getStackTraceString(Throwable tr);
    //public native  int println(int priority, java.lang.String tag, java.lang.String msg);
}
