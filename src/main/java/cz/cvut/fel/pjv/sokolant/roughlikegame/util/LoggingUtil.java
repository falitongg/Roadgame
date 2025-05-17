package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

import java.io.IOException;
import java.util.logging.*;

public class LoggingUtil {

    public static void setup() {
        Logger logger = Logger.getLogger("");
        logger.setUseParentHandlers(false);

        for(Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        //logging off
        String flag = System.getProperty("LOGGING", "true");
        if(!Boolean.parseBoolean(flag)) {
            logger.setLevel(Level.OFF);
            return;
        }

        //logging on
        try{
            FileHandler fh = new FileHandler("logs/game.txt");
        }catch (IOException e){
            e.printStackTrace();
        }

        logger.setLevel(Level.ALL);
    }
}
