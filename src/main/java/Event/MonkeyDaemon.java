package Event;

import io.appium.java_client.ios.IOSDriver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import static Untils.MyLogger.log_info;

public class MonkeyDaemon extends TimerTask {

    public static int loopTime = 60 * 1000; // 每隔5s执行一次
    private static String UDID;
    private static String BUNDLEID;


    public MonkeyDaemon(String UDID, String BUNDLEID) {
        this.UDID = UDID;
        this.BUNDLEID = BUNDLEID;
    }

    @Override
    public void run() {
        if (UDID.contains("-")) {
            log_info("模拟器检查app运行状态!");
            String runcmd = "xcrun simctl spawn booted log stream --level=debug";
            Process pp = null;
            try {
                pp = Runtime.getRuntime().exec(runcmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            int lineCount = 0;
            String runstring = "";
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    lineCount++;
                    if (lineCount < 100) {
                        runstring += line + "\n";
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //System.out.println("===" + runstring + "===");
                if (runstring.contains("LuoJiFMIOS")) {
                    log_info("模拟器app运行中!");
                } else {
                    log_info("模拟器app不在运行中!");
                    try {
                        Runtime.getRuntime().exec("xcrun simctl launch booted " + BUNDLEID);
                        log_info("====非测试APP，重新呼起测试APP====");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            log_info("真机检查app运行状态!");
        }
    }
}
