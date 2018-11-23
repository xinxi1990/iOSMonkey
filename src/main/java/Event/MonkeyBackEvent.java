package Event;

import io.appium.java_client.ios.IOSDriver;
import static Untils.MyLogger.log_info;


/**
 * @author xinxi
 * Monkey返回事件
 */


public class MonkeyBackEvent extends MonkeyEvent {
    private IOSDriver driver;

    public MonkeyBackEvent(IOSDriver driver) {
        super(MonkeyEvent.EVENT_TYPE_BACK);
        this.driver = driver;
    }


    public int injectEvent() throws Exception {
        try {
            driver.navigate().back();
            log_info("sending Back Event Over!");;
            return MonkeyEvent.INJECT_SUCCESS;
        }catch (Exception E){
            log_info(this.getClass().getName() + "-" + "injectEvent异常:" + E);
            return INJECT_FAIL;
        }
    }

}



