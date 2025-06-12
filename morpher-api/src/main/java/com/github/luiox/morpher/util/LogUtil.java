package com.github.luiox.morpher.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;

public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger("morpher-core-console");
    private static LogLevel logLevel = LogLevel.INFO;
    private static int indent = 0;
    private static String IndetnString = " ".repeat(2);

    public static void setShowLevel(LogLevel level) {
        logLevel = level;
    }

    private static boolean isLoggable(LogLevel level) {
        return level.getLevel() >= logLevel.getLevel();
    }

    private static void log(Consumer<Logger> loggerAction, LogLevel level) {
        if (isLoggable(level)) {
            loggerAction.accept(logger);
        }
    }

    public static void increaseIndent() {
        indent++;
    }

    public static void decreaseIndent() {
        indent--;
    }

    private static String formatMessage(String format) {
        if (indent == 0) {
            return format;
        }
        return IndetnString.repeat(indent - 1) + "- " + format;
    }

    private static @NotNull String formatMessage(@NotNull String format, Object[] args) {
        String formattedMessage = MessageFormatter.arrayFormat(format, args).getMessage();
        if (indent == 0) {
            return formattedMessage;
        }
        return IndetnString.repeat(indent - 1) + "- " + formattedMessage;
    }

    public static void debug(String message) {
        log(logger -> logger.debug(formatMessage(message)), LogLevel.DEBUG);
    }

    public static void info(String message) {
        log(logger -> logger.info(formatMessage(message)), LogLevel.INFO);
    }

    public static void error(String message) {
        log(logger -> logger.error(formatMessage(message)), LogLevel.ERROR);
    }

    public static void debug(String format, Object... args) {
        log(logger -> logger.debug(formatMessage(format, args)), LogLevel.DEBUG);
    }

    public static void info(String format, Object... args) {
        log(logger -> logger.info(formatMessage(format, args)), LogLevel.INFO);
    }

    public static void error(String format, Object... args) {
        log(logger -> logger.error(formatMessage(format, args)), LogLevel.ERROR);
    }

    public enum LogLevel {
        DEBUG(0),
        INFO(1),
        ERROR(2);

        private final int level;

        LogLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    public static void printStackTrace(@NotNull Throwable throwable) {
        // 同时输出到终端和文件
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        LogUtil.error("stacktrace:{}", stringWriter.toString());

//        throwable.printStackTrace(System.err);
    }
}
