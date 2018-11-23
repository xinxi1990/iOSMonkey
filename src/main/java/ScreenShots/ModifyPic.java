package ScreenShots;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static Untils.MyLogger.log_info;


public class ModifyPic {
	private Font font = new Font("", Font.PLAIN, 20);// 添加字体的属性设置
	private Graphics2D g = null;
	private int fontsize;
	private int x;
	private int y;
	private String imgSrcPath;
	private String imgDesPath;
	private String content;
	private String fontStyle;
    private Color color;
    
	/**
	 * 导入本地图片到缓冲区
	 */
	public BufferedImage loadImageLocal(String imgSrcPath) {
		try {
			this.imgSrcPath = imgSrcPath;
			return ImageIO.read(new File(this.imgSrcPath));
		} catch (IOException e) {
			log_info(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 生成新图片到本地
	 */
	public void writeImageLocal(String imgDesPath, BufferedImage img) {
		if (imgDesPath != null && img != null) {
			try {
				this.imgDesPath = imgDesPath;
				File outputfile = new File(this.imgDesPath);
				ImageIO.write(img, "png", outputfile);
			} catch (IOException e) {
				log_info(e.getMessage());
			}
		}
	}
 
	/**
	 * 设定文字的字体等
	 */
	public void setFont(String fontStyle, int fontSize) {
		this.fontsize = fontSize;
		this.fontStyle = fontStyle;
		this.font = new Font(this.fontStyle, Font.PLAIN, this.fontsize);
	}
	
	/**
	 * 设定文字在图片中的位置以及文字的颜色
	 * @param x
	 * @param y
	 * @param color
	 */
    public void setLocalColor(int x,int y,Color color){
			this.x = x;
			this.y = y;
			this.color=color;
    }
    
	/**
	 * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
	 */
	public BufferedImage modifyImage(BufferedImage img, String content) {
		try {
			int w = img.getWidth();
			int h = img.getHeight();
			g = img.createGraphics();
			//g.setBackground(Color.WHITE);
			g.setColor(this.color);
			if (this.font != null)
				g.setFont(this.font);
			// 验证输出位置的纵坐标和横坐标
			if (x >= h || y >= w) {
				this.x = h - this.fontsize + 2;
				this.y = w;
			} 
			this.content=content;
			if (content != null) {
				g.drawString(this.content, this.x, this.y);
			}
			g.dispose();
		} catch (Exception e) {
			log_info(e.getMessage());
		}
		return img;
	}
	

	public ModifyPic(String imgSrcPath, String imgDesPath, String content,
			String fontStyle, int fontsize, int x, int y,Color color) {
		super();
		this.fontsize = fontsize;
		this.x = x;
		this.y = y;
		this.imgSrcPath = imgSrcPath;
		this.imgDesPath = imgDesPath;
		this.content = content;
		this.fontStyle = fontStyle;
		this.color=color;
		setFont(fontStyle, fontsize);
		writeImageLocal(imgDesPath, modifyImage(loadImageLocal(imgSrcPath), content));
	}


	public ModifyPic(String fontStyle, int fontsize, int x, int y,Color color) {
		super();
		this.fontsize = fontsize;
		this.x = x;
		this.y = y;
		this.fontStyle = fontStyle;
		this.color=color;
		setFont(fontStyle, fontsize);
		setLocalColor(x,y,color);
	}


}
