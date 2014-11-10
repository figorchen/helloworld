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
 * ��ȡ��ַ���������ݿ�
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
	 * @param webSite ��ַ
	 */
	public SearchWebInfoClass(String webSite) {
		super();
		this.webSite = "http://" + webSite;
	}
	/**
	 * �չ��캯��
	 */
	public SearchWebInfoClass() {
		super();
	}
	
	/**
	 * �����վ��ҳ����
	 * @return String����
	 * @throws Exception
	 * @throws Exception
	 */
	private String connectUrl() throws Exception, Exception {
		String strHtml = "", charSet = "";
		//̽����ҳ����
		charSet = getFileEncoding(new URL(this.webSite));
		//����URLConnection����
		URLConnection uc = new URL(this.webSite).openConnection();
		//����IO���󣬶�ȡҳ�����ݣ�����ΪcharSet
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
	    /* �������
	    File file = new File("D:\\caiji.html");  
        file.delete();  
        file.createNewFile();  
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(  
                new FileOutputStream(file), "utf-8"));  
        writer.write(strHtml);  
        writer.close();*/
	}
	
	/**
	 * ̽��ҳ�����
	 * @param url url����
	 * @return String����  ����
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
	 * ��֤ҳ������ ����������Ϣ
	 * @param strHtml ҳ������
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
		
		frist = Pattern.compile("((qq)|(QQ))(��|(����))?.{0,3}\\s*(<.?w*>)*(\\d{6,10})");
		matcher = frist.matcher(strHtml);
		while(matcher.find()) {
			this.qq += matcher.group() + "  ";
		}
		
		frist = Pattern.compile("��ϵ��\\s*.?\\s*([\\u4e00-\\u9fa5]{1,5})|([\\u4e00-\\u9fa5]{1,2}(����|����|����|Ůʿ|С��))");
		second = Pattern.compile("(([\\u4e00-\\u9fa5]){1,5}[^(��ϵ��)^��])|([\\u4e00-\\u9fa5]{1,2}(����|����|����|Ůʿ|С��))");
		matcher = frist.matcher(strHtml);
		while(matcher.find()) {
			String temp = matcher.group();
			matcherSecond = second.matcher(matcher.group());
			matcherSecond.find();
			this.contact += matcherSecond.group() + "  ";
		}
		
		frist = Pattern.compile("(�人)|(����)|(���)|(����)|(����)");
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
