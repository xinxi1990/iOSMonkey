package Event;


import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import java.util.List;
import java.util.Random;
import static Untils.MyLogger.log_info;

/**
 * @author xinxi
 * Monkey Xpath点击事件类
 */


public class MonkeyXpathEvent extends MonkeyEvent {

    private IOSDriver driver;
    private By xpath1= By.xpath("//*[contains(name(), 'Text') and" +
            " @name!='' and string-length(@label)<10]");
    private By xpath2= By.xpath("//*[contains(name(), 'Image') and @name!='']");

    public MonkeyXpathEvent(IOSDriver driver) {
        super(MonkeyEvent.EVENT_TYPE_CONTENT);
        this.driver = driver;
    }


    /**
    随机点击文本控件
     */
    public int injectEvent_Text() throws Exception {
        try {
            log_info("sending injectEvent Text!");
            Random ra = new Random();
            List ellist1 = driver.findElements(xpath1);
            if (ellist1.size() > 0 ){
                int randomEl1 =  ra.nextInt(ellist1.size() + 1);
                IOSElement iosElement = (IOSElement) ellist1.get(randomEl1);
                iosElement.click();
                log_info(String.format(this.getClass().getName() + "-" + "injectEvent_Text click %s!", randomEl1));
                return MonkeyEvent.INJECT_SUCCESS;
            }else {
                log_info(this.getClass().getName() + "-" + "injectEvent_Text miss!");
                return MonkeyEvent.INJECT_FAIL;
            }
        }catch (Exception E){
            log_info(this.getClass().getName() + "-" + "injectEvent异常:" + E);
            return MonkeyEvent.INJECT_FAIL;
        }
    }


    /**
     随机点击图标控件
     */
    public int injectEvent_Image() throws Exception {
        try {
            log_info("sending injectEvent Image!");
            Random ra = new Random();
            List ellist1 = driver.findElements(xpath2);
            if (ellist1.size() > 0 ){
                int randomEl1 =  ra.nextInt(ellist1.size() + 1);
                IOSElement iosElement = (IOSElement) ellist1.get(randomEl1);
                iosElement.click();
                log_info(String.format(this.getClass().getName() + "-" + "injectEvent_Image click %s!", randomEl1));
                return MonkeyEvent.INJECT_SUCCESS;
            }else {
                log_info(this.getClass().getName() + "-" + "injectEvent_Image miss!");
                return MonkeyEvent.INJECT_FAIL;
            }
        }catch (Exception E){
            log_info(this.getClass().getName() + "-" + "injectEvent异常:" + E);
            return MonkeyEvent.INJECT_FAIL;
        }
    }


    @Override
    public int injectEvent() throws Exception {
        return 0;
    }
}
