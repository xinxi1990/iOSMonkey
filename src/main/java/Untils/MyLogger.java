package Untils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class MyLogger {

    public static Logger logger;

    /**
     * 初始化logger
     */
    public static Logger initLogger(){
        logger = Logger.getLogger("iOSMonkey");
        return logger;
    }

    /**
     * info日志
     */
    public static void log_info(String text){
        initLogger().info(text);
    }

    /**
     * debug日志
     */
    public static void log_debug(String text){
        initLogger().info(text);
    }

    public static void main(String[] args) {
        initLogger().setLevel(Level.ALL);
        initLogger().info("test111");

    }



}
