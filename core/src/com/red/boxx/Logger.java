package com.red.boxx;

/**
 * The type Logger.
 */
public class Logger {

    private Boolean debuggingMode;

    /**
     * Instantiates a new Logger.
     *
     * @param debuggingMode the debugging mode
     */
    Logger(Boolean debuggingMode) {
        setDebuggingMode(debuggingMode);
    }

    /**
     * Log.
     *
     * @param type         the type
     * @param insideMethod the inside method
     * @param message      the message
     */
    void log(LoggingType type, String insideMethod, String message) {

        String typeAsString;

        if (type == LoggingType.DEBUG && !this.debuggingMode) {
            return;
        }
        if (type == LoggingType.INFO) {
            typeAsString = "INFO";
        } else if (type == LoggingType.DEBUG) {
            typeAsString = "DEBUG";
        } else if (type == LoggingType.WARNING) {
            typeAsString = "WARNING";
        } else /* (type == LoggingType.ERROR) */ {
            typeAsString = "ERROR";
        }

    }

    /**
     * Gets debugging mode.
     *
     * @return the debugging mode
     */
    public Boolean getDebuggingMode() {
        return debuggingMode;
    }

    /**
     * Sets debugging mode.
     *
     * @param debuggingMode the debugging mode
     */
    private void setDebuggingMode(Boolean debuggingMode) {
        this.debuggingMode = debuggingMode;
    }

    /**
     * The enum Logging type.
     */
    public enum LoggingType {
        /**
         * Debug logging type.
         */
        DEBUG,
        /**
         * Info logging type.
         */
        INFO,
        /**
         * Warning logging type.
         */
        WARNING,
        /**
         * Error logging type.
         */
        ERROR
    }

}