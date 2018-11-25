import Driver.DriverServer;
import Event.*;
import Untils.GetPerformance;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.openqa.selenium.Dimension;
import ScreenShots.Screenshot;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import static Event.MonkeyDaemon.loopTime;
import static Untils.FileUntils.createGif;
import static Untils.FileUntils.readYaml;
import static Untils.MyLogger.initLogger;
import static Untils.MyLogger.log_info;
import static Untils.SearchCrash.*;


public class Monkey {

    public static IOSDriver iosDriver;
    private int width,height, submitX_mim,
            submitX_max, submitY_mim,
            submitY_max, contentX_min,
            contentX_max, contentY_mim,
            contentY_max, special_point_x,
            special_point_y;
    private static boolean needhelp = false;
    private static String ConfigPath;
    private static String UDID;
    private static String BUNDLEID;
    private static String NeedScreenshot;
    private static String PORT;
    private static String PROXYPORT;
    private static String VERBOSE;
    private static String TRACEPATH;
    private static String ScreenshotFolder;
    private static String AppiumLogFolder;
    private static String TestCase;
    private static String Need = "true";
    private static Dimension dimension;
    private boolean is_running = true;
    private int eventcount = 0;
    private static String TIMING;
    private DriverServer driverServer;
    private static long EventWaitTime = 100;
    private static int ScreentCount = 0;
    private static boolean NEEDHELP = false;



    public static void main(String[] args) throws Exception {
        executeParameter(args);
    }


    /**
     * 执行参数
     * @param args
     * @throws Exception
     */
    private static void executeParameter(String[] args) {
        int optSetting = 0;

        for (; optSetting < args.length; optSetting++) {
            for (; optSetting < args.length; optSetting++) {
                if ("-f".equals(args[optSetting])) {
                    ConfigPath = args[++optSetting];
                } else if ("-h".equals(args[optSetting])) {
                    NEEDHELP = true;
                    System.out.println("-f:配置文件");
                    break;
                }
            }
        }
        if (!needhelp) {
            Map configMap = readYaml(ConfigPath);
            UDID = (String) configMap.get("UDID");
            BUNDLEID = (String) configMap.get("BUNDLEID");
            TIMING = (String) configMap.get("TIMING");
            PORT = (String) configMap.get("PORT");
            PROXYPORT = (String) configMap.get("PROXYPORT");
            VERBOSE = (String) configMap.get("VERBOSE");
            NeedScreenshot = (String) configMap.get("NeedScreenshot");
            ScreenshotFolder = (String) configMap.get("ScreenshotFolder");
            VERBOSE = (String) configMap.get("VERBOSE");
            TRACEPATH = (String) configMap.get("TRACEPATH");
            AppiumLogFolder = (String) configMap.get("AppiumLogFolder");
            TestCase = (String) configMap.get("TestCase");

            try {
                log_info(
                        "\n" + "测试设备:" + UDID +
                             "\n" + "测试App:" + BUNDLEID +
                             "\n" + "Appium端口:" + PORT +
                             "\n" + "WDA端口:" + PROXYPORT +
                             "\n" + "是否需要截图:" + NeedScreenshot +
                             "\n" + "是否需要打印log:" + VERBOSE);
                if (VERBOSE.equals(Need)){
                    initLogger().setLevel(Level.ALL);
                }else {
                    initLogger().setLevel(Level.WARN);
                }
                org.testng.Assert.assertTrue((!UDID.equals(null)) && (!BUNDLEID.equals(null)));
            } catch (Exception e) {
                log_info("请确认参数配置,需要帮助请输入 java -jar iosMonkey.jar -h\n"
                        + "ERROR信息"+ e.toString());
            }
            try {
                new Monkey().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 运行monkey主方法
     * @throws Exception
     */
    public void run() throws Exception {
        log_info("查询是否有缓存崩溃文件!");
        backup();
        driverServer = new DriverServer(UDID,Integer.parseInt(PORT),Integer.parseInt(PROXYPORT));
        driverServer.killWDAServer();
        driverServer.killAppiumServer();
        driverServer.startAppiumServer(AppiumLogFolder);
        iosDriver = driverServer.baseDriver(BUNDLEID);
        Screenshot SC = new Screenshot(UDID,ScreenshotFolder);
        log_info("本次设定的运行时长【" + TIMING + "】分钟");
        long startTime = System.currentTimeMillis();
        Map coordinateMap = calculateCoordinate(iosDriver);

        if (!TestCase.equals("")){
            log_info("用例文件地址:" + TestCase);
            new CaseEvent(iosDriver).generateCase(TestCase); //生成case
        }else {
            log_info("未配置测试用例,进行Monkey测试!");
        }

        if (NeedScreenshot.equals(Need)) {
            File outputPath = new File(Screenshot.ScreenshotFolder);
            if (outputPath.exists()){
                FileUtils.deleteQuietly(new File(String.valueOf(outputPath)));
                log_info(String.format("删除%s",outputPath));
                FileUtils.forceMkdir(outputPath);
                log_info(String.format("创建%s",outputPath));
            }else {
                FileUtils.forceMkdir(outputPath);
                log_info(String.format("创建%s",outputPath));
            }
        }

        Timer timer = new Timer();
        //延迟0秒，并且每过5s执行一次
        timer.schedule(new MonkeyDaemon(UDID,BUNDLEID), 0, loopTime);

        GetPerformance getPerformance= new GetPerformance();
        HashMap<String, Object> args = getPerformance.startTrace(iosDriver,TRACEPATH);
        try {
            while (is_running) {
                new MonkeySpecialEvent(iosDriver).closeAlert();
                switch (new MathRandom().PercentageRandom()) {
                    case 0: {
                        int x = (int) Math.ceil(Math.random() * (width - 1));
                        int y = (int) Math.ceil(Math.random() * (height - 1));
                        if (NeedScreenshot.equals(Need)) {
                            SC.screenshot(ScreentCount,new Double(x).intValue(), new Double(y).intValue());
                            ScreentCount ++;
                        }
                        new MonkeyTapEvent(iosDriver, x, y).injectEvent();
                        eventcount = eventcount+1;
                        log_info("---EVENT执行了："+eventcount+"次---");
                        break;
                    }
                    case 1: {
                        new MonkeySwipeEvent(iosDriver).injectEvent_scrollDown();
                        eventcount = eventcount+1;
                        log_info("---EVENT执行了："+eventcount+"次---");
                        break;
                    }
                    case 2: {
                        new MonkeyBackEvent(iosDriver).injectEvent();
                        eventcount = eventcount+1;
                        log_info("---EVENT执行了："+eventcount+"次---");
                        break;
                    }
                    case 3: {
                        int x = randomSubmitCoordinate(coordinateMap)[0];
                        int y = randomSubmitCoordinate(coordinateMap)[1];
                        if (NeedScreenshot.equals(Need)){
                            SC.screenshot(ScreentCount,x ,y);
                            ScreentCount ++;
                        }
                        new MonkeySubmitEvent(iosDriver, x, y).injectEvent();
                        eventcount = eventcount+1;
                        log_info("---EVENT执行了："+eventcount+"次---");
                        break;
                    }
                    case 4: {
                        new MonkeyXpathEvent(iosDriver).injectEvent_Text();
                        eventcount = eventcount+1;
                        log_info("---EVENT执行了："+eventcount+"次---");
                        break;
                    }
                    case 5: {
                        new MonkeyHomeKeyEvent(iosDriver).injectEvent();
                        eventcount = eventcount+1;
                        log_info("---EVENT执行了："+eventcount+"次---");
                        break;
                    }
                }

                long endTime = System.currentTimeMillis();
                if((endTime - startTime) > (Integer.parseInt(TIMING) * 60 * 1000)) {
                    log_info("已运行" + (endTime - startTime)/60/1000 + "分钟，任务即将结束");

                    // 发送邮件
                    log_info("退出守护线程!");
                    is_running = false;
                    timer.cancel();
                }else {
                    Thread.sleep(EventWaitTime);
                    long runTime = (endTime - startTime) / 1000;
                    String p = getRunProgress(Integer.parseInt(TIMING) * 60, (int)runTime);
                    System.out.println(String.format("当前运行进度:%s",p));
                }
            }
            getPerformance.endTrace(iosDriver,args);
        }catch (Exception E){
            log_info(this.getClass().getName() + "-" + "Run异常:" + E);
        }

        if (NeedScreenshot.equals(Need)){
            createGif(Screenshot.ScreenshotFolder); //生成gif
        }
        teardown();
    }



    public void teardown() throws IOException {
        ArrayList crashList = searchCrashPath();
        log_info("搜索是否有崩溃日志");
        int crashSize = crashList.size();;
        if (crashList.size() !=0 ){
            String [] strings = (String[]) crashList.toArray(new String[crashSize]);
            for (Object str:strings ) {
                System.out.println(str);
            }
            System.out.println(merageCrashResult);
            if (mergeFiles(strings,merageCrashResult)){
            }else {
                log_info("合并crash文件失败!");
            }
        }else {
            log_info("本次运行monkey未发生崩溃!");
        }

    }


    /**
     * 计算运行进度,保留一位小数
     */
    public static String getRunProgress(int totalTime, int currentTime){
        try {
            float Progress = (float) currentTime / (float)totalTime;
            DecimalFormat decimalFormat= new DecimalFormat(".0");
            String p= decimalFormat.format(Progress * 100);
            return p ;
        }catch (Exception e) {
            log_info(String.format("计算运行进度异常!%s", e));
            return "0";
        }
    }



    /**
     * 计算坐标
     */
    private Map calculateCoordinate(IOSDriver iosDriver) throws Exception {
        dimension = iosDriver.manage().window().getSize();
        width= dimension.width;
        height= dimension.height;

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        submitX_max = width - 1;
        submitX_mim = width / 10;
        submitY_max = height - 1;
        submitY_mim = height / 10 * 9;
        contentX_max = width - width / 10;
        contentX_min = width / 10;
        contentY_max = height / 2 + height / 10;
        contentY_mim = height / 2 - height / 10;
        special_point_x = width / 2;
        special_point_y = (int) (height * 0.94);

        Map Coordinatemap = new HashMap();
        Coordinatemap.put("submitX_max",submitX_max);
        Coordinatemap.put("submitX_mim",submitX_mim);
        Coordinatemap.put("submitY_max",submitY_max);
        Coordinatemap.put("submitY_mim",submitY_mim);
        Coordinatemap.put("contentX_max",contentX_max);
        Coordinatemap.put("contentX_min",contentX_min);
        Coordinatemap.put("contentY_max",contentY_max);
        Coordinatemap.put("contentY_mim",contentY_mim);
        Coordinatemap.put("special_point_x",special_point_x);
        Coordinatemap.put("special_point_y",special_point_y);
        return Coordinatemap;
    }

    /**
     * 计算随机坐标
     */
    private int[] randomContentCoordinate(Map coordinateMap) throws Exception {
        Random random = new Random();
        contentX_max = (int) coordinateMap.get("contentX_max");
        contentX_min = (int) coordinateMap.get("contentX_min");
        contentY_max = (int) coordinateMap.get("contentY_max");
        contentY_mim = (int) coordinateMap.get("contentY_mim");
        int x = random.nextInt(contentX_max) % (contentX_max - contentX_min + 1) + contentX_min;
        int y = random.nextInt(contentY_max) % (contentY_max - contentY_mim + 1) + contentY_mim;
        int[] array = {x, y};
        return array;
    }


    /**
     * 计算随机坐标
     */
    private int[] randomSubmitCoordinate(Map coordinateMap) throws Exception {
        try {
            Random random = new Random();
            submitX_max = (int) coordinateMap.get("submitX_max");
            submitX_mim = (int) coordinateMap.get("submitX_mim");
            submitY_max = (int) coordinateMap.get("submitY_max");
            submitY_mim = (int) coordinateMap.get("submitY_mim");
            int x = random.nextInt(submitX_max) % (submitX_max - submitX_mim + 1) + submitX_mim;
            int y = random.nextInt(submitY_max) % (submitY_max - submitY_mim + 1) + submitY_mim;
            int[] array = {x, y};
            return array;
        }catch (Exception E){
            log_info(this.getClass().getName() + "-" + "计算随机坐标异常:" + E);
            int[] array = {0, 0};
            return array;
        }
    }
}
