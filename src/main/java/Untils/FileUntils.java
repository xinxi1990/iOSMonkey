package Untils;

import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static Untils.MyLogger.log_info;


public class FileUntils {


	public static String timeDate(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestr = dateFormat.format(now);
		return timestr;
	}

	/**
     * 用当前日期生成唯一序列号
     * 格式：年+月+日+小时+分钟
	 * 20150421113300
	 * @return 
     */ 
	public static String currentTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		Calendar cal = Calendar.getInstance();
		String times = format.format(cal.getTime()).substring(0,19);
		Pattern pattern = Pattern.compile("[^0-9]");  
		Matcher matcher = pattern.matcher(times); 
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sbr,"");
		}
		matcher.appendTail(sbr);
		//System.out.println(sbr.toString());
		return sbr.toString();	
	}
	
	public static void sleep(double d) {
		try {
			d *= 1000;
			Thread.sleep((int)d);
		} catch (Exception e){}
	}


	/**
	 * 读Yaml文件
	 * param YamlName
	 * return Map类型
	 */
	public static Map readYaml(String yamlpath) {

		Map<String, String> map = null;
		try{
			File dumpFile = new File(yamlpath);
			Yaml yaml = new Yaml();
			map = (Map<String, String>) yaml.load((new FileInputStream(dumpFile)));
		}catch (Exception e){
			System.out.println(String.format("读取%s异常! + \n + %s", yamlpath,e));
		}
		return map;
	}

	/**
	 * 生成gif
	 */
	public static void createGif(String folderPath){
		try {
			String pngPath = folderPath + "iOSMonkey%03d.png";
			String gifPath = folderPath + "iOSMonkey.gif";
			String cmd = "ffmpeg -f image2 -framerate 1 -i " + pngPath + " -y " + gifPath;
			log_info(String.format("合并gif命令:%s", cmd));
			Runtime.getRuntime().exec(cmd);
			log_info("生成gif地址:" + gifPath );
		}catch (Exception E){
			log_info("生成gif异常:" + E);
		}
	}

	public static void main(String[] args) {
		Map map = readYaml("./config/config.yaml");
		System.out.println(map);
	}



}
