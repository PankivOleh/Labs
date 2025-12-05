package Utils;

import java.io.IOException;
import java.util.logging.*;

public class LogSetup {

    public static void setup() throws IOException {
        Logger rootLogger = Logger.getLogger("");

        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        FileHandler fileHandler = new FileHandler("app.log", true);

        fileHandler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";
            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new java.util.Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });

        fileHandler.setLevel(Level.ALL);
        rootLogger.addHandler(fileHandler);

        rootLogger.setLevel(Level.ALL);
    }
}