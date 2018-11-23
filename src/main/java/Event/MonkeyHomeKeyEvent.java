package Event;

import static Untils.MyLogger.log_info;
import io.appium.java_client.ios.IOSDriver;


/**
 * @author xinxi
 * Monkey重新启动app事件类
 */


public class MonkeyHomeKeyEvent extends MonkeyEvent{
	private String UDID, BUNDLEID;
    public static IOSDriver iosDriver;

    public MonkeyHomeKeyEvent(IOSDriver driver) {
        super(MonkeyEvent.EVENT_TYPE_HOMEKEY);
        this.iosDriver = driver;
    }

    public int injectEvent() throws Exception {
    	log_info("sending HOMEKEY Event.");
        iosDriver.closeApp();
        iosDriver.launchApp();
        return MonkeyEvent.INJECT_SUCCESS;
    } 
}
