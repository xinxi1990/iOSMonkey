import java.io.*;
import java.nio.Buffer;
import java.sql.Time;
import java.text.DecimalFormat;

import static Untils.MyLogger.log_info;

public class Test {


    public static void main(String[] args) throws InterruptedException, IOException {

//        while (true){
//            new Test().run("DDAC13B0-786D-4DC7-A920-4BEAF56CD616");
//            Thread.sleep(1000);
//        }

        //System.out.println(getRunProgress(3600,2000));

        long a = System.currentTimeMillis();
        System.out.println(a);
        Thread.sleep(2000);
        long b = System.currentTimeMillis();
        System.out.println(b);
        System.out.println(b-a);




    }



    public void run(String UDID) {
        if (UDID.contains("-")){
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
                    if (lineCount < 5) {
                        runstring += line + "\n";
                    }else {
                        break;
                    }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                System.out.println(runstring);
                if (runstring.contains("LuoJiFMIOS")) {
                    log_info("模拟器app运行中!");
                } else {
                    log_info("模拟器app不在运行中!");
                    log_info("====非测试APP，重新呼起测试APP====");
                    //Runtime.getRuntime().exec("xcrun simctl launch booted " + BUNDLEID);
                }
            }
        }else {
            log_info("真机检查app运行状态!");
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




}
