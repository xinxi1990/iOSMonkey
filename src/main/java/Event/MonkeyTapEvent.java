package Event;

import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;
import static Untils.MyLogger.log_info;


/**
 * @author xinxi
 * Monkey点击事件类
 */

public class MonkeyTapEvent extends MonkeyEvent{
    private int x, y;
    private IOSDriver driver;

    public MonkeyTapEvent(IOSDriver driver, int x, int y) {
        super(MonkeyEvent.EVENT_TYPE_TAP);
        this.x = x;
        this.y = y;
        this.driver = driver;
    }

    public int injectEvent() throws Exception {
        try {
            log_info("sending Tap Event : Tap->(" + x + ", " + y + ")");
            TouchAction touchAction = new TouchAction(driver);
            touchAction.tap(PointOption.point(x, y)).perform();
            log_info("sending Tap Event Over!");
            return MonkeyEvent.INJECT_SUCCESS;
        }catch (Exception E){
            log_info(this.getClass().getName() + "-" + "injectEvent异常:" + E);
            return INJECT_FAIL;
        }
    }
}
