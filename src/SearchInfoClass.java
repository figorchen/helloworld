import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JFrame;

public class SearchInfoClass {
	public static void main(String agrs[]) {
		/*ReadFileClass words = new ReadFileClass("D:\\keywords.txt");
		FindUrlClass test = new FindUrlClass();
		ArrayList keywords = words.getKeyWords();
		ArrayList urlList = new ArrayList();
		for (int i = 0 ;i < keywords.size(); i++){
			test.setUrl("http://www.baidu.com/s?wd=" + (String)keywords.get(i));
			try {
				test.manageGetLink();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			urlList.addAll(test.getUrlList());
			words.addKeyWordValue((String)keywords.get(i), test.getUrlList().size());
			System.out.println(i);
		}
		System.out.println(words.getKeyWordValue());
		
		dbConnClass database = new dbConnClass("127.0.0.1", "3306", "customer", "root", "chenkeke");
		for(int i = 0 ;i < urlList.size(); i++) {
			database.setSql("select id from info where website = 'http://"+(String)urlList.get(i) + "'");
			try {
				database.connectDB();
				if(database.selectDB() != 0) {
					urlList.remove(i);
					System.out.println(urlList.get(i));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		/*try {
			System.out.println(webinfo.createSql());
		} catch (Throwable e) {
			e.printStackTrace();
		}*/
		/*for(int i = 0 ;i < urlList.size() ; i++) {
			webinfo.setWebSite((String)urlList.get(i));
			try {
				System.out.println(webinfo.createSql());
				database.setSql(webinfo.createSql());
				database.updateDB();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		MainFrameClass theFrame = new MainFrameClass();
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);theFrame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - theFrame.getSize().width) / 2,(Toolkit.getDefaultToolkit().getScreenSize().height - theFrame.getSize().height) / 2);  
		//System.out.print(HttpTookit.doGet("http://www.baidu.com/s", "wd=Æû³µÎ¬ÐÞ&pn=0", "utf-8", false));
	}
}
