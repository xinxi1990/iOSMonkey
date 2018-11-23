package Event;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.log4testng.Logger;
import java.time.Duration;
import java.util.HashMap;
import static Untils.MyLogger.log_info;


/**
 * @author xinxi
 * appium基本操作封装类
 */

public abstract class FunctionEvent {

    public final AppiumDriver<?> driver;
    public String PageSource;
    public Logger logger;
    public Boolean is_exist;
    private Dimension dimension;
    private TouchAction touchAction;
    private long sleepTime = 300;
    private int MaxScroll = 10;
    private int MaxBack = 10;

    public FunctionEvent(AppiumDriver<?> driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        touchAction = new TouchAction(this.driver);
    }


    /**
     * 查询元素是否存在
     * return: bool
     */
    public Boolean findElementsBy(By by) throws InterruptedException {
        sleep(1);
        is_exist = false;
        if (driver.findElements(by).size() > 0){
            log_info(String.format("查询到元素:%s",by));
            is_exist = true;
        }else {
            is_exist = false;
        }
        return is_exist;
    }


    /**
     * 页面是否包含某个文案
     * return:bool
     */
    public Boolean findElements(String text) throws InterruptedException {
        this.sleep(1);
        is_exist = false;
        PageSource = this.driver.getPageSource();
        if (PageSource.contains(text)){
            is_exist = true;

        }else {
            is_exist = false;
        }
        return is_exist;
    }


    /**
     * 断言页面包含某个的元素
     */
    public void assertContain(String text) throws InterruptedException {
        this.sleep(2);
        PageSource = this.driver.getPageSource();
        if (PageSource.contains(text)){
            Assert.assertTrue(true);
        }else {
            Assert.assertFalse(true);
        }
    }


    /**
     * 断言页面不包含某个的元素
     */
    public void assertNotContain(String text){
        PageSource = this.driver.getPageSource();
        if (!PageSource.contains(text)){
            Assert.assertTrue(true);
        }else {
            Assert.assertFalse(true);
        }

    }

    /**
     * 断言页面包含element
     * by:元素
     */
    public void assertContainBy(By by){

        if (driver.findElements(by).size() > 0){
            Assert.assertTrue(true);
        }else {
            Assert.assertFalse(true);
        }

    }


    /**
     * 断言页面不包含element
     * by:元素
     */
    public void assertNotContainBy(By by){

        if (!(driver.findElements(by).size() > 0)){
            Assert.assertTrue(true);
        }else {
            Assert.assertFalse(true);
        }
    }


    /**
     * 等待
     */
    public void sleep(int time) throws InterruptedException {
        Thread.sleep(1000 * time);
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



    /**
     * 向下滚动到某个元素
     */
    public void scrollDown(String value) throws InterruptedException {
        sleep(1);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap scrollObject = new HashMap();
        scrollObject.put("direction", "down");
        scrollObject.put("name", value);
        js.executeScript("mobile: scroll", scrollObject);
        log_info(String.format("向下滑动到元素:%s", value));
    }



    /**
     * 向上滚动到某个元素
     */
    public void scrollUp(String value){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap scrollObject = new HashMap();
        scrollObject.put("direction", "up");
        scrollObject.put("name", value);
        js.executeScript("mobile: scroll", scrollObject);
        log_info(String.format("向上滑动到元素:%s", value));
    }


    /**
     * 向下滚动到某个元素
     */
    public void scrollDownElement(String value) throws InterruptedException {
        while (MaxScroll > 0 ){
            if (findElements(value)){
                break;
            }else {
                scrollDown(1);
                MaxScroll --;
            }
        }
    }


    /**
     * 返回键
     */
    public void goBack(){
        driver.navigate().back();
    }

    /**
     * 循环返回到某个位置
     */
    public void loopBack(By by) throws InterruptedException {
        while (MaxBack > 0){
            if (findElementsBy(by)){
                break;
            }else {
                goBack();
                MaxBack --;
            }
        }
    }


    /**
     * 点击引导蒙层
     */
    public void tapGurid(int tapCount){


    }

}