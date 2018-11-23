package Event;

import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import java.time.Duration;
import static Untils.MyLogger.log_info;


/**
 * @author xinxi
 * Monkey滑动类
 */

public class MonkeySwipeEvent extends MonkeyEvent {
    private Dimension dimension;
    private TouchAction touchAction;
    private long sleepTime = 300;
    private IOSDriver driver;
    private int MaxScroll = 1;


    public MonkeySwipeEvent(IOSDriver driver) {
        super(MonkeyEvent.EVENT_TYPE_SWIPE);
        this.driver = driver;
    }

    @Override
    public int injectEvent() throws Exception {
        return 0;
    }

    /**
     * 向上滚动事件
     */
    public int injectEvent_scrollUp() throws Exception {
        try {
            log_info("sending scrollUp Event!");
            this.scrollUp(MaxScroll);
            log_info("sending scrollUp Over!");
            return MonkeyEvent.INJECT_SUCCESS;
        }catch (Exception E){
            log_info(this.getClass().getName() + "-" + "injectEvent异常:" + E);
            return INJECT_FAIL;
        }
    }


    /**
     * 向下滚动事件
     */
    public int injectEvent_scrollDown() throws Exception {
        try {
            log_info("sending scrollDown Event!");
            this.scrollDown(MaxScroll);
            log_info("sending scrollDown Over!");
            return MonkeyEvent.INJECT_SUCCESS;
        }catch (Exception E){
            log_info(this.getClass().getName() + "-" + "injectEvent异常:" + E);
            return INJECT_FAIL;
        }
    }


    /**
     * 获取屏幕尺寸
     */
    private Dimension getWindowSize(){
        dimension = driver.manage().window().getSize();
        return dimension;
    }

    /**
     * 向上滚动
     * @ looper 次数
     */
    public  void  scrollUp(int loop) throws InterruptedException {
        for (int i = 0; i < loop ; i++) {
            touchAction = new TouchAction(this.driver);
            touchAction.press(PointOption.point((int) (getWindowSize().width * 0.5),(int) (getWindowSize().height * 0.3)));
            touchAction.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
            touchAction.moveTo(PointOption.point((int) (getWindowSize().width * 0.5),(int) (getWindowSize().height * 0.8)));
            touchAction.release();
            touchAction.perform();
            log_info("向上滑动!");
            Thread.sleep(sleepTime);
        }
    }




    /**
     * 向上滚动
     * @ looper 次数
     */
    public  void  scrollDown(int loop) throws InterruptedException {
        for (int i = 0; i < loop ; i++) {
            touchAction = new TouchAction(this.driver);
            touchAction.press(PointOption.point((int) (getWindowSize().width * 0.5),(int) (getWindowSize().height * 0.8)));
            touchAction.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
            touchAction.moveTo(PointOption.point((int) (getWindowSize().width * 0.5),(int) (getWindowSize().height * 0.3)));
            touchAction.release();
            touchAction.perform();
            log_info("向下滑动!");
            Thread.sleep(sleepTime);
        }
    }



}
