package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

import java.io.IOException;
import java.util.logging.*;

/**
 * Utility class for configuring Java's global logging system.
 * Allows enabling or disabling logging based on a system property.
 * If logging is enabled, messages are written to a file named {@code log.txt}.
 */
public class LoggingUtil {

    /**
     * Sets up the global logger configuration.
     * Logging is directed to a file {@code log.txt} if enabled,
     * otherwise it is turned off completely.
     *
     * System property: {@code LOGGING=true|false}
     */
    public static void setup() {
        Logger logger = Logger.getLogger("");
        logger.setUseParentHandlers(false);

        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        // Check system property to determine if logging is enabled
        String flag = System.getProperty("LOGGING", "true");
        if (!Boolean.parseBoolean(flag)) {
            logger.setLevel(Level.OFF);
            return;
        }

        // Set up file loggingd
        try {
            FileHandler fh = new FileHandler("log.txt");
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.setLevel(Level.INFO);
    }
}
