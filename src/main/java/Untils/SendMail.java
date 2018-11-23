package Untils;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import static Untils.MyLogger.log_info;

/**
 * @author xinxi
 * 发送测试报告
 */

public class SendMail {
    public static Properties properties;
    public static String from;
    public static String username;
    public static String password;
    public static String host;
    public static Session session;
    public static String Subject;
    public static String Content = getCurrentTime() + "\n" + String.format("具体%s详见附件!", Subject) +
            "\n" + "如附件中文件格式丢失,请手动改成.html格式!";
    private static String mailist;
    private static String newPathName;
    private static String taskOver = "完成";

    public static void main(String [] args) throws IOException, InterruptedException {


    }


    /**
     * 拼接接收者地址
     * @param mailist
     * @return
     */
    public static  InternetAddress[]  Address(String mailist){
        //多个接收账号
        InternetAddress[] address=null;
        try {
            List list = new ArrayList();
            //不能使用string类型的类型，这样只能发送一个收件人
            String []median=mailist.split(",");
            //对输入的多个邮件进行逗号分割
            for(int i=0;i<median.length;i++){
                list.add(new InternetAddress(median[i]));
            }
            address =(InternetAddress[])list.toArray(new InternetAddress[list.size()]);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return address;
    }


    public static String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String CurrentTime = df.format(new Date());
        log_info("当前时间:" + CurrentTime);
        return CurrentTime;
    }

    /**
     * 设置邮件服务器参数、服务器端口等参数
     */
    public void setProperties(){
        from = MailConfig.mail_user;
        username = MailConfig.mail_user;
        password = MailConfig.mail_pwd;
        host = MailConfig.mail_host;
        properties = new Properties();
        properties.put("proxyHost",MailConfig.mail_postfix);
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.auth", "true");
        //properties.put("mail.debug", "true");
        //输出发送邮件的debug信息
        properties.put("mail.transport.protocol", "smtp");
        //设置Session对象，同时配置验证方法
        session = Session.getInstance(properties,new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        });
    }


    public void send(File attachment,InternetAddress[] address){
        try{
            //创建Message对象，并设置相关参数
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,address);
            //设置发送信息主题.信息正文
            message.setSubject(Subject);
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(Content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);

            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }

            // 将multipart对象放到message中
            message.setContent(multipart);
            message.saveChanges();
            Transport.send(message);
            log_info("发送成功!");
        }catch(MessagingException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }



}
