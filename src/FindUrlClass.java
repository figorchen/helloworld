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
 * 查找url类
 * @param url 用来储存搜索需要的url
 * @param urlList  储存最后查询到的所有网站链接
 * @param page  百度搜索页数标记，初始直接为0  代表从第0个记录开始  也是第一页  后面是10、20
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
	 * @param url 构造函数，初始化url
	 */
	public FindUrlClass(String url) {
		super();
		this.keyWord = url;
	}
	public FindUrlClass() {
		super();
	}
	
	/**
	 * 通过url获得百度搜索页面内容
	 * @param url 百度搜索链接
	 * @param page  控制搜索页面的翻页
	 * @throws Throwable 
	 * @throws MalformedURLException 
	 * @return 返回获取到的百度页面字串
	 */
	 public String getPage(String url, String queryString) { 
         StringBuffer response = new StringBuffer(); 
         HttpClient client = new HttpClient(); 
         HttpMethod method = new GetMethod(url); 
         try { 
             //对get请求参数做了http请求默认编码，好像没有任何问题，汉字编码后，就成为%式样的字符串 
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
	 * 通过url获得百度搜索页面内容(不带页数)
	 * @param url 百度搜索链接
	 * @throws Throwable 
	 * @throws MalformedURLException 
	 * @return 返回获取到的百度页面字串
	 */
	private String getPage(String url) throws MalformedURLException, Throwable {
		if(url == "") {
			return "";
		}
		String strHtml = "";
		//创建URLConnection对象
		URLConnection uc = new URL(url).openConnection();
		//创建IO对象，读取页面内容，编码为utf-8
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
	 * 控制整体流程的方法
	 * @throws Throwable 
	 * @throws MalformedURLException 
	 */
	public void manageGetLink() throws MalformedURLException, Throwable {
		for(;this.page <= 20; this.page += 10) {
			getLink(getPage("http://www.baidu.com/s", "wd=" + this.keyWord + "&pn=" + this.page));
		}
	}
	
	/**
	 * 通过页面获取link的方法
	 * @param strHtml 页面内容
	 * @throws Throwable 
	 * @throws MalformedURLException 
	 */
	private void getLink(String strHtml) throws MalformedURLException, Throwable {
		//判断页面内容是否为空，是则直接跳出函数
		if (strHtml == "") {
			return;
		}
		String tempLink = "";
		//正则表达式  模糊匹配右侧链接内容
		Pattern right = Pattern.compile("<font size=\"-1\" class=\"\\w*\">([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)</font>");
        //正则表达式  模糊匹配左侧链接内容
		Pattern left = Pattern.compile("<span class=\"\\w+\">\\s?([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)\\s?</span>\\s?<span class=\"\\w+\">\\s?\\d\\d\\d\\d-\\d\\d\\s?</span>\\s?<div id=\"tools_300\\d\"");
        //正则表达式  精确匹配域名
		Pattern yuming = Pattern.compile("([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)");
        Pattern exter = Pattern.compile("\"http://search\\.baidu\\.com/s\\?[\\S]*\"");
		Matcher matcher = right.matcher(strHtml);
        Matcher yumingMatcher = null;
        while(matcher.find()){
            //匹配到右侧链接模糊内容  保存在tempLink临时字串中
        	tempLink = matcher.group();
        	yumingMatcher = yuming.matcher(tempLink);
        	yumingMatcher.find();
        	tempLink = yumingMatcher.group();
        	this.urlList.add(tempLink);
        }
        matcher = left.matcher(strHtml);
        while(matcher.find()){
        	//匹配到左侧链接模糊内容  保存在tempLink临时字串中
        	tempLink = matcher.group();
        	yumingMatcher = yuming.matcher(tempLink);
        	yumingMatcher.find();
        	tempLink = yumingMatcher.group();
        	this.urlList.add(tempLink);
        }
        //检查是否有隐藏推广链接
        matcher = exter.matcher(strHtml);
        if(matcher.find()) {
        	tempLink = matcher.group();
            //检查出有隐藏链接  获取隐藏页面
            strHtml = getPage(tempLink.replace("\"", ""));
            //匹配隐藏页面内链接
            Pattern exterBlur = Pattern.compile("<span class=\"\\S+\">([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)</span>\\s*<span class=\"\\S+\">\\d\\d\\d\\d-\\d\\d</span>");
            matcher = exterBlur.matcher(strHtml);
            while(matcher.find()){
            	//匹配到隐藏链接模糊内容  保存在tempLink临时字串中
            	tempLink = matcher.group();
            	yumingMatcher = yuming.matcher(tempLink);
            	yumingMatcher.find();
            	tempLink = yumingMatcher.group();
            	this.urlList.add(tempLink);
            }
        }
        //检测是否有重复链接   有则删除
        HashSet hSet = new HashSet(urlList);
		urlList.clear();
		urlList.addAll(hSet);
	}
}
