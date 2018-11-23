package Untils;

import org.apache.http.util.TextUtils;
import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static Untils.MyLogger.log_info;

/**
 * @author xinxi
 * 运行完monkey查询崩溃日志
 */


public class SearchCrash {

    public static String startStr = "xxxxxxxx";
    public static String endStr = ".crash";
    public static String rootPath;

    static {
        try {
            rootPath = getRootPath() +"/Library/Logs/DiagnosticReports";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String backupPath;

    static {
        try {
            backupPath = getRootPath() + "/Library/Logs/DiagnosticReports/backups";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String merageCrashResult;

    static {
        try {
            merageCrashResult = String.format( getRootPath() + "/Library/Logs/DiagnosticReports/ios_crash_%s.log", getCurrentTime());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //backup();
//        ArrayList arrayList = new ArrayList();
//        arrayList.add("/Users/xinxi/Library/Logs/DiagnosticReports/backups/LuoJiFMIOS_2018-11-15-143820_xinxideMacBook-Pro.crash");
//        arrayList.add("/Users/xinxi/Library/Logs/DiagnosticReports/backups/LuoJiFMIOS_2018-11-15-143839_xinxideMacBook-Pro.crash");
//        int size = arrayList.size();
//        String [] strings = (String[]) arrayList.toArray(new String[size]);
//        mergeFiles(strings,merageCrashResult);

        System.out.println(getRootPath());

    }


    /**
     * 查询模拟崩溃日志全路径
     */
    public static ArrayList searchCrashPath() throws IOException {
        ArrayList arrayList = new ArrayList();
        InputStreamReader stdISR = null;
        InputStreamReader errISR = null;
        String[] command = { "sh", "-c", "ls " + rootPath };
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            String line = null;
            stdISR = new InputStreamReader(process.getInputStream());
            BufferedReader stdBR = new BufferedReader(stdISR);
            while ((line = stdBR.readLine()) != null) {
                if (line.startsWith(startStr) && line.endsWith(endStr)){
                    String fullPath = rootPath + "/" + line;
                    arrayList.add(fullPath);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return arrayList;
    }



    /**
     * 查询模拟崩溃日志
     */
    public static ArrayList searchCrashLogs() throws IOException {
        ArrayList arrayList = new ArrayList();
        InputStreamReader stdISR = null;
        InputStreamReader errISR = null;
        String[] command = { "sh", "-c", "ls " + rootPath };
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            String line = null;
            stdISR = new InputStreamReader(process.getInputStream());
            BufferedReader stdBR = new BufferedReader(stdISR);
            while ((line = stdBR.readLine()) != null) {
                if (line.startsWith(startStr) && line.endsWith(endStr)){
                    arrayList.add(line);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    /**
     * 备份崩溃日志
     */
    public static void backup() throws IOException {
        ArrayList arrayList = searchCrashLogs();
        Process process = null;
        if (arrayList.size() != 0){
            for (Object source:arrayList) {
                System.out.println(source);
                try {
                    String sourcePath = rootPath + "/" + source;
                    String destinationPath = backupPath+ "/" + source;
                    String[] command = { "sh", "-c", String.format("mv %s %s",sourcePath,destinationPath)};
                    Runtime.getRuntime().exec(command);
                    log_info(String.format("从%s移动到%s",sourcePath,destinationPath));
                } catch (IOException e) {
                    //moving file failed.
                    e.printStackTrace();
                }
            }
            log_info("移动崩溃日志完成!");
        }else {
            log_info("未查询到崩溃日志!");
        }
    }



    /**
     * 获取电脑根目录地址
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getRootPath() throws IOException, InterruptedException {
        InputStreamReader stdISR = null;
        InputStreamReader errISR = null;
        Process process = null;
        String rootName = null;
        String[] command = {"sh", "-c", "echo $HOME"};
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            String line = null;
            stdISR = new InputStreamReader(process.getInputStream());
            BufferedReader stdBR = new BufferedReader(stdISR);
            while ((line = stdBR.readLine()) != null) {
                rootName = line;

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return rootName;



    }


    /**
     * 文件合并
     * @param fpaths
     * @param resultPath
     * @return
     */
    public static boolean mergeFiles(String[] fpaths, String resultPath) {
        if (fpaths == null || fpaths.length < 1 || TextUtils.isEmpty(resultPath)) {
            return false;
        }
        if (fpaths.length == 1) {
            return new File(fpaths[0]).renameTo(new File(resultPath));
        }
        File[] files = new File[fpaths.length];
        for (int i = 0; i < fpaths.length; i ++) {
            files[i] = new File(fpaths[i]);
            if (TextUtils.isEmpty(fpaths[i]) || !files[i].exists() || !files[i].isFile()) {
                return false;
            }
        }
        File resultFile = new File(resultPath);
        try {
            FileChannel resultFileChannel = new FileOutputStream(resultFile, true).getChannel();
            for (int i = 0; i < fpaths.length; i ++) {
                FileChannel blk = new FileInputStream(files[i]).getChannel();
                resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
                blk.close();
            }
            resultFileChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        for (int i = 0; i < fpaths.length; i ++) {
            files[i].delete();
        }
        return true;
    }

    public static String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String CurrentTime = df.format(new Date());
        log_info("当前时间:" + CurrentTime);
        return CurrentTime;
    }

}
