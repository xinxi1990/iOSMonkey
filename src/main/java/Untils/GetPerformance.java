package Untils;

import io.appium.java_client.ios.IOSDriver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author xinxi
 * 获取iOS性能日志
 */

public class GetPerformance {


    private File traceZip;

    public HashMap<String, Object> startTrace(IOSDriver iosDriver,String tracePath){
        traceZip = new File(tracePath + "/trace.zip");
        HashMap<String, Object> args = new HashMap<>();
        args.put("timeout", 60000);
        args.put("pid", "current");
        args.put("profileName", "Time Profiler");
        iosDriver.executeScript("mobile: startPerfRecord", args);
        return args;
    }


    public void endTrace(IOSDriver iosDriver, HashMap<String, Object> args) throws IOException {
        args = new HashMap<>();
        args.put("profileName", "Time Profiler");
        String b64Zip = (String)iosDriver.executeScript("mobile: stopPerfRecord", args);
        byte[] bytesZip = Base64.getMimeDecoder().decode(b64Zip);
        FileOutputStream stream = new FileOutputStream(traceZip);
        stream.write(bytesZip);
    }


}
