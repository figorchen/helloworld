import info.monitorenter.cpdetector.io.*;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.sql.*;

import java.nio.charset.*;

/**
 * 读取网址并存入数据库
 * @author Administrator
 *
 */
public class SearchWebInfoClass {
	private String webSite = "";
	private String name = "";
	private String cellPhone = "";
	private String phone = "";
	private String qq = "";
	private String contact = "";
	private boolean place = false;
	private String sql = "";
	
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = "http://" + webSite;
		this.name = "";
		this.cellPhone = "";
		this.phone = "";
		this.contact = "";
		this.place = false;
		this.qq = "";
		this.sql = "";
	}
	/**
	 * @param webSite 网址
	 */
	public SearchWebInfoClass(String webSite) {
		super();
		this.webSite = "http://" + webSite;
	}
	/**
	 * 空构造函数
	 */
	public SearchWebInfoClass() {
		super();
	}
	
	/**
	 * 获得网站首页内容
	 * @return String类型
	 * @throws Exception
	 * @throws Exception
	 */
	private String connectUrl() throws Exception, Exception {
		String strHtml = "", charSet = "";
		//探测网页编码
		charSet = getFileEncoding(new URL(this.webSite));
		//创建URLConnection对象
		URLConnection uc = new URL(this.webSite).openConnection();
		//创建IO对象，读取页面内容，编码为charSet
		InputStream in = new BufferedInputStream(uc.getInputStream());
		StringBuffer temp = new StringBuffer();
	    Reader rd = new InputStreamReader(in,charSet);
	    int c = 0;
	    while ((c = rd.read()) != -1) {
	        temp.append((char) c);
	    }
	    in.close();
	    strHtml = temp.toString();
	    return strHtml;
	    /* 输出测试
	    File file = new File("D:\\caiji.html");  
        file.delete();  
        file.createNewFile();  
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(  
                new FileOutputStream(file), "utf-8"));  
        writer.write(strHtml);  
        writer.close();*/
	}
	
	/**
	 * 探测页面编码
	 * @param url url对象
	 * @return String类型  编码
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private String getFileEncoding(URL url) {
	    CodepageDetectorProxy codepageDetectorProxy = CodepageDetectorProxy.getInstance();
	    codepageDetectorProxy.add(JChardetFacade.getInstance());
	    codepageDetectorProxy.add(ASCIIDetector.getInstance());
	    codepageDetectorProxy.add(UnicodeDetector.getInstance());
	    codepageDetectorProxy.add(new ParsingDetector(false));
	    codepageDetectorProxy.add(new ByteOrderMarkDetector());
	    Charset charset;
		try {
			charset = codepageDetectorProxy.detectCodepage(url);
			return charset.name();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "utf-8";
	}
	
	/**
	 * 验证页面内容 并获得相关信息
	 * @param strHtml 页面内容
	 */
	private void checkInfo(String strHtml) {
		Pattern frist = Pattern.compile("<\\s*((title)|(TITLE))\\s*>[\\s\\S]*</\\s*((title)|(TITLE))\\s*>");
		Pattern second = Pattern.compile("[\\u4e00-\\u9fa5]*");
		Matcher matcher = frist.matcher(strHtml);
		Matcher matcherSecond = null;
		if(matcher.find()) {
			this.name = matcher.group().replaceAll("<.?\\s*((title)|(TITLE))\\s*>", "");
		}
		
		frist = Pattern.compile("((13[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8}");
		matcher = frist.matcher(strHtml);
		while(matcher.find()) {
			this.cellPhone += matcher.group() + "  ";
		}
		
		frist = Pattern.compile("(\\(?027\\)?\\s*-?\\s*\\d{8})|(400.?\\s?\\d.?\\s?\\d{3}.?\\s?\\d{3})");
		matcher = frist.matcher(strHtml);
		while(matcher.find()) {
			this.phone += matcher.group() + "  ";
		}
		
		frist = Pattern.compile("((qq)|(QQ))(号|(号码))?.{0,3}\\s*(<.?w*>)*(\\d{6,10})");
		matcher = frist.matcher(strHtml);
		while(matcher.find()) {
			this.qq += matcher.group() + "  ";
		}
		
		frist = Pattern.compile("联系人\\s*.?\\s*([\\u4e00-\\u9fa5]{1,5})|([\\u4e00-\\u9fa5]{1,2}(经理|主管|先生|女士|小姐))");
		second = Pattern.compile("(([\\u4e00-\\u9fa5]){1,5}[^(联系人)^：])|([\\u4e00-\\u9fa5]{1,2}(经理|主管|先生|女士|小姐))");
		matcher = frist.matcher(strHtml);
		while(matcher.find()) {
			String temp = matcher.group();
			matcherSecond = second.matcher(matcher.group());
			matcherSecond.find();
			this.contact += matcherSecond.group() + "  ";
		}
		
		frist = Pattern.compile("(武汉)|(湖北)|(武昌)|(汉口)|(汉阳)");
		matcher = frist.matcher(strHtml);
		while(matcher.find()) {
			this.place = true;
		}
		if(this.cellPhone.length() > 0) {
			this.cellPhone = this.cellPhone.substring(0, this.cellPhone.indexOf(" ")) + " ";
		}
		if(this.phone.length() > 0) {
			this.phone = this.phone.substring(0, this.phone.indexOf(" ")) + " ";
		}
		if(this.qq.length() > 0) {
			this.qq = this.qq.substring(0, this.qq.indexOf(" ")) + " ";
		}
		if(this.contact.length() > 250) {
			this.contact = this.contact.substring(0, 250);
		}
		if(this.name.length() > 250) {
			this.name = this.name.substring(0, 250);
		}
	}
	
	public String createSql() throws Throwable {
		String strHtml = "";
		strHtml = this.connectUrl();
		this.checkInfo(strHtml);
		sql = "insert into info (name, cellphone, phone, qq, contact, place, website) value('"
			+ this.name +"', '" + this.cellPhone + "', '" + this.phone + "', '" + this.qq + "', '" 
			+ this.contact + "', '" + this.place + "', '" + this.webSite + "')";
		return this.sql;
	}
	
}
