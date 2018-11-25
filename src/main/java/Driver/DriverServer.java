package Driver;

import java.io.*;
import java.net.URL;

import static Untils.FileUntils.timeDate;
import static Untils.MyLogger.log_info;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author xinxi
 * 创建appium driver
 */

public class DriverServer {

    public static IOSDriver iosDriver;
    public static  AndroidDriver androidDriver;
    private static String rootPath = System.getProperty("user.dir");
    private static String AppiumLog = "/" +timeDate() +"_appium.log";
    private static long AppiumWaitTime = 20 * 1000; // Appium Server启动时间
    private static int AppiumPort = 4723;
    private static String AppiumHost = "127.0.0.1";
    private static int GlobleWaitTime = 30 * 1000; // 设置全局隐示等待
    private static String udid;
    private static int appiumPort;
    private static int wdaPORT;

    public DriverServer(String UDID,int AppiumPort,int WDAPORT) {
        udid = UDID;
        appiumPort = AppiumPort;
        wdaPORT = WDAPORT;
    }




    /**
     * 组装Appium的capabilities
     * return: driver实例
     */
    public IOSDriver baseDriver(String BUNDLEID) throws IOException, InterruptedException {
        DesiredCapabilities caps=new DesiredCapabilities();
        caps.setCapability("platformName", "ios");
        caps.setCapability("automationName", "xcuitest");
        caps.setCapability("deviceName", "iPhone");
        caps.setCapability("udid", udid);
        caps.setCapability("app", BUNDLEID);
        //caps.setCapability("newCommandTimeout", 1800);
        //caps.setCapability("wdaConnectionTimeout", 1800*1000);
        caps.setCapability("wdaLocalPort", Integer.parseInt(String.valueOf(wdaPORT)));
        iosDriver = new IOSDriver(new URL(String.format("http://127.0.0.1:%s/wd/hub", appiumPort)),caps);
        log_info(String.format("capabilities配置:%s", iosDriver.getCapabilities()));
        log_info("app启动!");
        return iosDriver;
    }


    /**
     * 组装iOS的capabilities
     * return: driver实例
     */
    public IOSDriver IOSBaseDriver() throws IOException, InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String url = "http://"+ AppiumHost +":" + appiumPort + "/wd/hub";
        log_info("appium Remote URL:" + url);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone sim");
        //capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.2");
        capabilities.setCapability(MobileCapabilityType.UDID, udid);
        capabilities.setCapability(MobileCapabilityType.APP, "com.luojilab.LuoJiFM-IOS");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1800);
        capabilities.setCapability(IOSMobileCapabilityType.WDA_CONNECTION_TIMEOUT, 1800*1000);
        capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT,Integer.parseInt(String.valueOf(wdaPORT)));
        capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS,true);
        //capabilities.setCapability(IOSMobileCapabilityType.START_IWDP, true);
        //capabilities.setCapability("autoWebview", true);
        //capabilities.setCapability("xcodeOrgId", "7Q6C9D7LVN");
        // capabilities.setCapability("xcodeSigningId", "iPhone Developer");
        iosDriver = new IOSDriver(new URL(url),capabilities);
        log_info(String.format("capabilities配置:%s", iosDriver.getCapabilities()));
        log_info("app启动!");
        return iosDriver;
    }

    /**
     * 组装Android的capabilities
     * return: driver实例
     */
    public AndroidDriver AndroidBaseDriver() throws IOException, InterruptedException {
        DesiredCapabilities caps=new DesiredCapabilities();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, udid);
        capabilities.setCapability(MobileCapabilityType.UDID, udid);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1800);
        capabilities.setCapability("appPackage", "com.luojilab.player");
        //Android 7要用 uiautomator2
        //if(getSDKVersion(udid) > 23){
        //    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        //    log.info("Using uiautomator2");
        //}
        String url = "http://"+ AppiumHost +":" + appiumPort + "/wd/hub";
        androidDriver = new AndroidDriver(new URL(url),caps);
        log_info("appium URL:" + url);
        log_info(String.format("capabilities配置:%s", iosDriver.getCapabilities()));
        log_info("app启动!");
        return androidDriver;
    }



    /**
     * 启动appium server
     */
    public  void startAppiumServer(String AppiumLogFolder) throws IOException, InterruptedException {
        String startCmd = String.format("appium -a %s -p %s --log %s --relaxed-security",AppiumHost,appiumPort,AppiumLogFolder + AppiumLog);
        log_info("appium运行命令:" + startCmd);
        Runtime.getRuntime().exec(startCmd);
        Thread.sleep(AppiumWaitTime);
    }



    /**
     * 启动前杀死appium server进程
     */
    public  void killAppiumServer() throws IOException{
        String serachCmd = String.format("lsof -i:%s", appiumPort);
        Process p = Runtime.getRuntime().exec(serachCmd);
        InputStream is = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line ;
        while ((line = reader.readLine()) != null) {
            if (line.contains(String.valueOf("TCP"))){
                String[] tt=line.split("\\s+");
                String killCmd = String.format("kill -9 %s", tt[1]);
                log_info(String.format("kill AppiumServer命令:%s", killCmd));
                Runtime.getRuntime().exec(killCmd);
                break;
            }
        }
        is.close();
        reader.close();
        p.destroy();
    }

    /**
     * 启动前杀死WDA server进程
     */
    public  void killWDAServer() throws IOException{
        String serachCmd = String.format("lsof -i:%s", wdaPORT);
        Process p = Runtime.getRuntime().exec(serachCmd);
        InputStream is = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line ;
        while ((line = reader.readLine()) != null) {
            if (line.contains(String.valueOf("TCP"))){
                String[] tt=line.split("\\s+");
                String killCmd = String.format("kill -9 %s", tt[1]);
                log_info(String.format("kill WDAServer命令:%s", killCmd));
                Runtime.getRuntime().exec(killCmd);
                break;
            }
        }
        is.close();
        reader.close();
        p.destroy();
    }



}
