package com.whammich.invasion.util;

import com.mojang.logging.LogUtils;
import com.whammich.invasion.Reference;
import org.slf4j.Logger;

/**
 * Logging helper for Invasion.
 */
public final class LogHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    private LogHelper() {
    }

    public static void info(String message, Object... args) {
        LOGGER.info("[{}] " + message, prepend(args));
    }

    public static void warn(String message, Object... args) {
        LOGGER.warn("[{}] " + message, prepend(args));
    }

    public static void error(String message, Object... args) {
        LOGGER.error("[{}] " + message, prepend(args));
    }

    public static void debug(String message, Object... args) {
        LOGGER.debug("[{}] " + message, prepend(args));
    }

    private static Object[] prepend(Object... args) {
        Object[] out = new Object[args.length + 1];
        out[0] = Reference.MODID;
        System.arraycopy(args, 0, out, 1, args.length);
        return out;
    }
}
