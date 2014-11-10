import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.httpclient.HttpClient; 
import org.apache.commons.httpclient.HttpMethod; 
import org.apache.commons.httpclient.HttpStatus; 
import org.apache.commons.httpclient.URIException; 
import org.apache.commons.httpclient.methods.GetMethod; 
import org.apache.commons.httpclient.methods.PostMethod; 
import org.apache.commons.httpclient.params.HttpMethodParams; 
import org.apache.commons.httpclient.util.URIUtil; 
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/**
 * ����url��
 * @param url ��������������Ҫ��url
 * @param urlList  ��������ѯ����������վ����
 * @param page  �ٶ�����ҳ����ǣ���ʼֱ��Ϊ0  ����ӵ�0����¼��ʼ  Ҳ�ǵ�һҳ  ������10��20
 **/
public class FindUrlClass {
	private static Log log = LogFactory.getLog(FindUrlClass.class); 
	private String keyWord = "";
	private ArrayList urlList = new ArrayList();
	private int page = 0;
	
	private static int n = 0;
	
	public String getUrl() {
		return keyWord;
	}
	public void setKeyword(String url) {
		this.keyWord = url;
		this.urlList.removeAll(urlList);
		this.page = 0;
	}
	public ArrayList getUrlList() {
		return urlList;
	}

	/**
	 * @param url ���캯������ʼ��url
	 */
	public FindUrlClass(String url) {
		super();
		this.keyWord = url;
	}
	public FindUrlClass() {
		super();
	}
	
	/**
	 * ͨ��url��ðٶ�����ҳ������
	 * @param url �ٶ���������
	 * @param page  ��������ҳ��ķ�ҳ
	 * @throws Throwable 
	 * @throws MalformedURLException 
	 * @return ���ػ�ȡ���İٶ�ҳ���ִ�
	 */
	 public String getPage(String url, String queryString) { 
         StringBuffer response = new StringBuffer(); 
         HttpClient client = new HttpClient(); 
         HttpMethod method = new GetMethod(url); 
         try { 
             //��get�����������http����Ĭ�ϱ��룬����û���κ����⣬���ֱ���󣬾ͳ�Ϊ%ʽ�����ַ��� 
             method.setQueryString(URIUtil.encodeQuery(queryString)); 
             client.executeMethod(method); 
             if (method.getStatusCode() == HttpStatus.SC_OK) { 
            	 BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "utf-8")); 
                 int line; 
                 while ((line = reader.read()) != -1) { 
                 	response.append((char)line); 
                 } 
                 reader.close(); 
                 
                 
                 /*File file = new File("D:\\baidu"+ n +".html"); 
         	     n++; 
                 file.delete();  
                 file.createNewFile();  
         	     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));  
                 writer.write(response.toString());  
                 writer.close();*/
             } 
         } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  finally { 
                 method.releaseConnection(); 
         } 
         return response.toString(); 
 } 
	
	/**
	 * ͨ��url��ðٶ�����ҳ������(����ҳ��)
	 * @param url �ٶ���������
	 * @throws Throwable 
	 * @throws MalformedURLException 
	 * @return ���ػ�ȡ���İٶ�ҳ���ִ�
	 */
	private String getPage(String url) throws MalformedURLException, Throwable {
		if(url == "") {
			return "";
		}
		String strHtml = "";
		//����URLConnection����
		URLConnection uc = new URL(url).openConnection();
		//����IO���󣬶�ȡҳ�����ݣ�����Ϊutf-8
		InputStream in = new BufferedInputStream(uc.getInputStream());
		StringBuffer temp = new StringBuffer();
	    Reader rd = new InputStreamReader(in,"UTF-8");
	    int c = 0;
	    while ((c = rd.read()) != -1) {
	        temp.append((char) c);
	    }
	    in.close();
	    strHtml = temp.toString();
	    //
	    File file = new File("D:\\baidu"+ n +".html"); 
	    n++;
        file.delete();  
        file.createNewFile();  
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(  
                new FileOutputStream(file), "utf-8"));  
        writer.write(strHtml);  
        writer.close();
	    //
		return strHtml;
	}
	
	/**
	 * �����������̵ķ���
	 * @throws Throwable 
	 * @throws MalformedURLException 
	 */
	public void manageGetLink() throws MalformedURLException, Throwable {
		for(;this.page <= 20; this.page += 10) {
			getLink(getPage("http://www.baidu.com/s", "wd=" + this.keyWord + "&pn=" + this.page));
		}
	}
	
	/**
	 * ͨ��ҳ���ȡlink�ķ���
	 * @param strHtml ҳ������
	 * @throws Throwable 
	 * @throws MalformedURLException 
	 */
	private void getLink(String strHtml) throws MalformedURLException, Throwable {
		//�ж�ҳ�������Ƿ�Ϊ�գ�����ֱ����������
		if (strHtml == "") {
			return;
		}
		String tempLink = "";
		//������ʽ  ģ��ƥ���Ҳ���������
		Pattern right = Pattern.compile("<font size=\"-1\" class=\"\\w*\">([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)</font>");
        //������ʽ  ģ��ƥ�������������
		Pattern left = Pattern.compile("<span class=\"\\w+\">\\s?([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)\\s?</span>\\s?<span class=\"\\w+\">\\s?\\d\\d\\d\\d-\\d\\d\\s?</span>\\s?<div id=\"tools_300\\d\"");
        //������ʽ  ��ȷƥ������
		Pattern yuming = Pattern.compile("([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)");
        Pattern exter = Pattern.compile("\"http://search\\.baidu\\.com/s\\?[\\S]*\"");
		Matcher matcher = right.matcher(strHtml);
        Matcher yumingMatcher = null;
        while(matcher.find()){
            //ƥ�䵽�Ҳ�����ģ������  ������tempLink��ʱ�ִ���
        	tempLink = matcher.group();
        	yumingMatcher = yuming.matcher(tempLink);
        	yumingMatcher.find();
        	tempLink = yumingMatcher.group();
        	this.urlList.add(tempLink);
        }
        matcher = left.matcher(strHtml);
        while(matcher.find()){
        	//ƥ�䵽�������ģ������  ������tempLink��ʱ�ִ���
        	tempLink = matcher.group();
        	yumingMatcher = yuming.matcher(tempLink);
        	yumingMatcher.find();
        	tempLink = yumingMatcher.group();
        	this.urlList.add(tempLink);
        }
        //����Ƿ��������ƹ�����
        matcher = exter.matcher(strHtml);
        if(matcher.find()) {
        	tempLink = matcher.group();
            //��������������  ��ȡ����ҳ��
            strHtml = getPage(tempLink.replace("\"", ""));
            //ƥ������ҳ��������
            Pattern exterBlur = Pattern.compile("<span class=\"\\S+\">([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)</span>\\s*<span class=\"\\S+\">\\d\\d\\d\\d-\\d\\d</span>");
            matcher = exterBlur.matcher(strHtml);
            while(matcher.find()){
            	//ƥ�䵽��������ģ������  ������tempLink��ʱ�ִ���
            	tempLink = matcher.group();
            	yumingMatcher = yuming.matcher(tempLink);
            	yumingMatcher.find();
            	tempLink = yumingMatcher.group();
            	this.urlList.add(tempLink);
            }
        }
        //����Ƿ����ظ�����   ����ɾ��
        HashSet hSet = new HashSet(urlList);
		urlList.clear();
		urlList.addAll(hSet);
	}
}
