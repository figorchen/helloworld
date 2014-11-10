import java.net.*;
import java.io.*;
import java.util.regex.*;
public class test {
	/*public static void main(String agrs[]) throws Exception {
		String http = "http://www.baidu.com/s?wd=花艺培训&pn=20", strHtml;
		URLConnection uc = new URL(http).openConnection();
		InputStream in = new BufferedInputStream(uc.getInputStream());
		StringBuffer temp = new StringBuffer();
	    Reader rd = new InputStreamReader(in,"UTF-8");
	    int c = 0;
	    while ((c = rd.read()) != -1) {
	        temp.append((char) c);
	    }
	    in.close();
	    strHtml = temp.toString();
	    String item[] = strHtml.split("id=\"300\\d\"");
	    
	    System.out.print(strHtml.contains("推广"));
	    //ReadWriteFileWithEncode.write("D:\\baidu.html", strHtml, "utf-8");
	    File file = new File("D:\\baidu.html");  
        file.delete();  
        file.createNewFile();  
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(  
                new FileOutputStream(file), "utf-8"));  
        writer.write(strHtml);  
        writer.close();
        
        String yuming = "";
        Pattern p = Pattern.compile("<font size=\"-1\" class=\"\\w*\">([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)</font>");
        Pattern p1 = Pattern.compile("<span class=\"\\w+\">\\s?([a-zA-Z0-9]{1,}\\.){1,3}(com|cn|net|org|gov|edu|int|hk)\\s?</span>\\s?<span class=\"\\w+\">\\s?\\d\\d\\d\\d-\\d\\d\\s?</span>\\s?<div id=\"tools_300\\d\"");
        Matcher matcher = p.matcher(strHtml);
        while(matcher.find()){
            //在匹配的区域，使用group,replace等进行查看和替换操作
        	yuming = matcher.group();
        	System.out.println(yuming);
        }
        matcher = p1.matcher(strHtml);
        while(matcher.find()){
            //在匹配的区域，使用group,replace等进行查看和替换操作
        	yuming = matcher.group();
        	System.out.println(yuming);
        }
	}*/
}
