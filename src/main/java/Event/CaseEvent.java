package Event;


import Driver.DriverServer;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static Untils.FileUntils.readYaml;
import static Untils.MyLogger.log_info;

/**
 * @author xinxi
 * 根据步骤生成自动化case
 */


public class CaseEvent {

    private static String TestCase = "testcase";
    private static String CaseName = "casename";
    private static String Steps = "steps";
    private static String When = "when";
    private static String By = "by";
    private static String Action = "action";
    private static String SleepTime = "sleeptime";
    private static String SendValue;
    private static String Method;
    private static String ElementValue;
    private static List ElementList;
    private static IOSDriver iosDriver;
    private static IOSElement iosElement;
    private static DriverServer driverServer;
    public static String projectPath = System.getProperty("user.dir");

    public CaseEvent(IOSDriver Driver) {
        this.iosDriver = Driver;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        driverServer = new DriverServer("606EC265-1ED8-454D-AF80-BEB78A417B55",4723,8001);
        driverServer.killWDAServer();
        driverServer.killAppiumServer();
        driverServer.startAppiumServer("./");
        iosDriver = driverServer.baseDriver("com.iOS.Demo");
    }


    public void generateCase(String casePath) throws InterruptedException {
        log_info("测试用例文件路径:" + casePath);
        Map caseYaml = readYaml(casePath);
        generateRule(caseYaml);
    }


    /**
     * 递归解析Case.yaml文件
     * @param caseYaml
     * @throws InterruptedException
     */
    private void generateRule(Map caseYaml) throws InterruptedException {
        for (Object key : caseYaml.keySet()) {
            Object value = caseYaml.get(key);
            if (key.toString().startsWith(TestCase)){
                log_info("测试用例:" + key);
                generateRule((Map) value);
            }else if (key.toString().startsWith(CaseName)){
                log_info("用例名字:" + value);
            }else if (key.toString().startsWith(Steps)){
                log_info("用例步骤:" + value);
                for (Object action : (ArrayList<String>) (value)) {
                    acitonGenerate((Map) action);
                }
            }
        }
    }


    /**
     * 映射appium操作元素的动作
     * @param actionMap
     * @throws InterruptedException
     */
    private void acitonGenerate(Map actionMap) throws InterruptedException {
        for (Object key : actionMap.keySet()) {
            Object value = actionMap.get(key);
            if (key.toString().startsWith(When)){
                if (!value.toString().equals("")){
                    Method = value.toString().split(";")[0];
                    ElementValue = value.toString().split(";")[1];
                    log_info("检查【" + ElementValue + "】是否存在");
                    if (Method.equals("name")){
                        ElementList = iosDriver.findElements(org.openqa.selenium.By.name(ElementValue));
                        if (ElementList.size() > 0){
                            log_info(ElementValue + "存在");
                            continue;
                        }else {
                            log_info(ElementValue + "不存在,剩余步骤不执行");
                            break;
                        }
                    }else if (Method.equals("xpath")){
                        ElementList = iosDriver.findElements(org.openqa.selenium.By.xpath(ElementValue));
                        if (ElementList.size() > 0){
                            log_info(ElementValue + "存在");
                            continue;
                        }else {
                            log_info(ElementValue + "不存在,剩余步骤不执行");
                            break;
                        }
                    }
                }else {
                    log_info("没有待检查元素");
                }

            }else if (key.toString().startsWith(By)){
                Method = value.toString().split(";")[0];
                ElementValue = value.toString().split(";")[1];
                log_info("定位元素方法:" + Method);
                log_info("定位元素值:" + ElementValue);
                if (Method.equals("name")){
                    iosElement = (IOSElement) iosDriver.findElement(org.openqa.selenium.By.name(ElementValue));
                }else if (Method.equals("xpath")){
                    iosElement = (IOSElement) iosDriver.findElement(org.openqa.selenium.By.xpath(ElementValue));
                }
            }else if (key.toString().startsWith(Action)){
                log_info("事件动作:" + value);
                if (value.toString().startsWith("click")){
                    iosElement.click();
                }else if (value.toString().startsWith("sendKeys")){
                    SendValue = value.toString().split(";")[1];
                    iosElement.clear();
                    iosElement.sendKeys(SendValue);
                    log_info("输入内容:" + SendValue);
                }
            }else if (key.toString().startsWith(SleepTime)){
                Thread.sleep(Long.parseLong(String.valueOf(value)));
                log_info("等待时间:" + value);
            }
        }

    }

}
