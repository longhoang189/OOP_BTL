package util;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class AppLogger {
    private static final Logger logger = Logger.getLogger("StudentManager");
    private static boolean initialized = false;

    public static Logger get() {
        if (!initialized) init();
        return logger;
    }

    private static synchronized void init() {
        if (initialized) return;
        try {
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);

            File dir = new File("logs");
            if (!dir.exists()) dir.mkdirs();

            FileHandler fh = new FileHandler("logs/app.log", 1024 * 1024, 3, true);
            fh.setEncoding("UTF-8");
            fh.setFormatter(new SimpleFormatter() {
                private static final String FMT = "[%1$tF %1$tT] [%2$s] %3$s%n";
                @Override
                public String format(LogRecord r) {
                    return String.format(FMT,
                            new java.util.Date(r.getMillis()),
                            r.getLevel().getLocalizedName(),
                            r.getMessage());
                }
            });
            logger.addHandler(fh);

            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.WARNING);
            ch.setFormatter(fh.getFormatter());
            logger.addHandler(ch);

            initialized = true;
        } catch (IOException e) {
            System.err.println("Không thể khởi tạo logger: " + e.getMessage());
        }
    }
}
