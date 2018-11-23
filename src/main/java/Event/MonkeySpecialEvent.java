package Event;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import java.util.ArrayList;
import static Untils.MyLogger.log_info;

public class MonkeySpecialEvent {

    public static IOSDriver iosDriver;
    private ArrayList arrayList;
    private By closeLogin = By.id("medal popview dismiss");
    private By sure = By.name("确定");
    private By freeSee = By.name("随便看看");
    private By cancel = By.name("取消");

    public MonkeySpecialEvent(IOSDriver driver) {
        iosDriver = driver;
        arrayList = new ArrayList();
        arrayList.add(closeLogin);
        arrayList.add(sure);
        arrayList.add(sure);
        arrayList.add(freeSee);
        arrayList.add(cancel);
    }


    /**
     * 关闭弹框
     */
    public void closeAlert() throws Exception {
        try {
            for (Object by : arrayList) {
                if (iosDriver.findElements((By) by).size() > 0) {
                    iosDriver.findElement((By) by).click();
                    log_info("关闭弹框");
                   }
               }
            }
            catch(Exception E){
                log_info(this.getClass().getName() + "-" + "closeAlert:" + E);
            }
    }
}