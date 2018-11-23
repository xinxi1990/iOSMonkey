package ScreenShots;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import static Untils.MyLogger.log_info;


/**
 * @author xinxi
 * 截图
 */

public class Screenshot {

	public static String projectPath = System.getProperty("user.dir");
	public static String ScreenshotFolder;
	public static String screenshotPath1 ;
	public static String screenshotPath2 ;
	public static String udid;

	public Screenshot(String UDID,String Folder) {
		udid = UDID;
		ScreenshotFolder = String.format(Folder + "/", udid);
		File file = new File(ScreenshotFolder);
		if (! file.exists()){
			file.mkdir();
			log_info("创建保存图片文件夹:" + ScreenshotFolder);
		}
	}

	public void screenshot(int COUNT, int x, int y) throws InterruptedException {
		//File file = new File(projectPath);
		//String RootPath = file.getParent();
		//log_info("文件的上级目录为 : " + RootPath);


		screenshotPath1 = ScreenshotFolder  + "iOSMonkey.png";
		screenshotPath2 = ScreenshotFolder + String.format("iOSMonkey%s.png",String.format("%03d", COUNT));
		String screenshotCmd = String.format("xcrun simctl io booted screenshot %s", screenshotPath1);
		log_info("截图命令:" + screenshotCmd);

		if (udid.contains("-")) {
			try {
				Process pp = Runtime.getRuntime().exec(screenshotCmd);
				pp.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Runtime.getRuntime().exec("idevicescreenshot -u " + udid + " " + screenshotPath1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Thread.sleep(500);
		File file1 = new File(screenshotPath1);
		if (file1.exists() && file1.length() > 0) {
			new ModifyPic(screenshotPath1, screenshotPath2, "◯", "STYLE_ITALIC", 50, x, y, Color.RED);
			log_info("create ScreenShots : " + screenshotPath2);
			file1.delete();
		} else if (file1.exists() && file1.length() <= 0) {
			file1.delete();
		} else {
			log_info("截图失败，并没有找到截图文件！");
		}

	}
}
