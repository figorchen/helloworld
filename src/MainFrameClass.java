import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLDocument.Iterator;

public class MainFrameClass extends JFrame {
	public static final int WIDTH = 380;
	public static final int HEIGHT = 240;
	private JLabel keywords, totalKeywords, nowKeywords, totalLinks, nowLinks, address, port, user, password;
	private JButton readButton, submitButton;
	private JTextField field = new JTextField(20);
	private JTextField addressField = new JTextField(16);
	private JTextField portField = new JTextField(6);
	private JTextField userField = new JTextField(10);
	private JPasswordField passwordField = new JPasswordField(13);
	public JPanel topJPanel, midJPanel, footJPanel, bigmidJPanel, topmidPanel;
	
	public MainFrameClass() {
		topJPanel = new JPanel();
		midJPanel = new JPanel();
		footJPanel = new JPanel();
		bigmidJPanel = new JPanel();
		topmidPanel = new JPanel();
		this.getContentPane().add(topJPanel, "North");
		this.getContentPane().add(bigmidJPanel, "Center");
		this.getContentPane().add(footJPanel, "South");
		bigmidJPanel.setLayout(new GridLayout(2, 1));
		midJPanel.setLayout(new GridLayout(2, 2));
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("采集推广客户");
		this.setVisible(true);
		keywords = new JLabel("选择文件");
		totalKeywords = new JLabel("                           共0个关键词，");
		nowKeywords = new JLabel("当前第0个");
		totalLinks = new JLabel("                               共0个链接，");
		nowLinks = new JLabel("当前第0个");
		address = new JLabel("IP地址:");
		port = new JLabel("端口号:");
		user = new JLabel("用户名:");
		password = new JLabel("密码:");
		readButton = new JButton("浏览");
		submitButton = new JButton("开始");
		topmidPanel.add(address);
		topmidPanel.add(addressField);
		topmidPanel.add(port);
		topmidPanel.add(portField);
		topmidPanel.add(user);
		topmidPanel.add(userField);
		topmidPanel.add(password);
		topmidPanel.add(passwordField);
		topJPanel.add(keywords);
		topJPanel.add(field);
		topJPanel.add(readButton);
		midJPanel.add(totalKeywords);
		midJPanel.add(nowKeywords);
		midJPanel.add(totalLinks);
		midJPanel.add(nowLinks);
		bigmidJPanel.add(topmidPanel);
		bigmidJPanel.add(midJPanel);
		footJPanel.add(submitButton);
		openFileAction fileListener = new openFileAction();
		readButton.addActionListener(fileListener);
		starFindAction start = new starFindAction();
		submitButton.addActionListener(start);
		this.setResizable(false);
		try {
			this.iniReader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class openFileAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			field.setText(openChooser());
		}
	}
	
	private String openChooser() {
		JFileChooser fileChooser = new JFileChooser();
		//添加过滤的两种方法：
		FileNameExtensionFilter ff = new FileNameExtensionFilter( null, "txt");
		fileChooser.setFileFilter(ff);
		int option = fileChooser.showOpenDialog(null);
		if(option == JFileChooser.APPROVE_OPTION){
		//获取基本信息
		return fileChooser.getSelectedFile().getPath();
		}
		return "";
	}
	
	private class starFindAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			starFind();
		}
	}
	
	private void starFind() {
		this.saveData();
		MainThreadClass thread = new MainThreadClass();
		Thread threadObj = new Thread(thread);
		threadObj.start();
	}
	
	public class MainThreadClass implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			dbConnClass database = new dbConnClass(addressField.getText(), portField.getText(), "customer", userField.getText(), passwordField.getText());
			database.connectDB();
			ReadFileClass words = new ReadFileClass(field.getText());
			FindUrlClass findurl = new FindUrlClass();
			
			//获得文件内所有关键词，返回关键词arraylink
			ArrayList keywords = words.getKeyWords();
			
			//检查关键词表内对应链接数少于5个的关键词 并添加进keywords
			ResultSet rs = null;
			try {
				rs = database.selectDBResult("select * from keywords where linkcount < 5");
				int j = rs.getRow();
				while (rs.previous()){
					String name = rs.getString("keyword");
					keywords.add(name);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			int keywordCount = keywords.size();
			//清理重复关键词
			HashSet keywordSet = new HashSet(keywords);
			keywords.clear();
			keywords.addAll(keywordSet);
			//保存关键词个数，输出总次数到主面板
			totalKeywords.setText(totalKeywords.getText().replace("0", String.valueOf(keywordCount)));
			
			ArrayList urlList = new ArrayList();
			//循环获得关键词所包含的链接数，
			int mount = 0;
			for (int i = 0 ;i < keywordCount; i++){
				//把当前正在查询的关键词索引赋值给当前关键词文本域
				nowKeywords.setText(nowKeywords.getText().replace(String.valueOf(i), String.valueOf(i+1)));
				//创建查找链接
				findurl.setKeyword((String)keywords.get(i));
				
				try {
					//开始进行查找请求
					findurl.manageGetLink();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//把当前关键词查到的链接添加进总链接arraylist
				urlList.addAll(findurl.getUrlList());
				//更新链接总数到
				totalLinks.setText(totalLinks.getText().replace(String.valueOf(mount), String.valueOf(urlList.size())));
				mount = urlList.size();
				//添加关键词--链接数到hashmap
				words.addKeyWordValue((String)keywords.get(i), findurl.getUrlList().size());
			}
			
			HashMap keywordValue = new HashMap();
			keywordValue.putAll(words.getKeyWordValue());
			Set keys = keywordValue.keySet();
			java.util.Iterator keyIter = keys.iterator();
			
			//把关键词和查询到的链接数添加进数据库
			while(keyIter.hasNext()){
				 String nextkey = (String) keyIter.next();
				 int linkcount =Integer.parseInt(keywordValue.get(nextkey).toString());
				 try {
					if(database.selectDB("select id from keywords where keyword = '" + nextkey + "'") > 0) {
						 database.updateDB("update keywords set linkcount = " + linkcount + " where keyword = '" + nextkey + "'");
					 } else {
						 database.updateDB("insert into keywords (keyword, linkcount) values ('" + nextkey + "', " + linkcount +")");
					 }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			nowKeywords.setText("完成！");
			////////////////////////查找链接个数结束////////////////////////////////////
			
			//检测是否有重复链接   有则删除
			HashSet hSet = new HashSet(urlList);
			urlList.clear();
			urlList.addAll(hSet);
			//检测数据库中是否有重复链接   有则删除
			for(int i = 0 ;i < urlList.size(); i++) {
				database.setSql("select id from info where website = 'http://"+(String)urlList.get(i) + "'");
				try {
					database.connectDB();
					if(database.selectDB() != 0) {
						urlList.remove(i);
						i--;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			totalLinks.setText(totalLinks.getText().replace(String.valueOf(mount), String.valueOf(urlList.size())));
			SearchWebInfoClass webinfo = new SearchWebInfoClass("");
			for(int i = 0 ;i < urlList.size() ; i++) {
				nowLinks.setText(nowLinks.getText().replace(String.valueOf(i), String.valueOf(i+1)));
				webinfo.setWebSite((String)urlList.get(i));
				try {
					database.setSql(webinfo.createSql());
					//System.out.println(database.getSql());
					database.updateDB();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			nowLinks.setText("完成！");
		}
	}
	
	private void iniReader() throws IOException {
		FileReader iniFile = new FileReader("D:\\search.ini");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(iniFile);
			String str = "";
			if((str = reader.readLine()) != null ) {
				field.setText(str);
			}
			if((str = reader.readLine()) != null ) {
				addressField.setText(str);
			}
			if((str = reader.readLine()) != null ) {
				portField.setText(str);
			}
			if((str = reader.readLine()) != null ) {
				userField.setText(str);
			}
			if((str = reader.readLine()) != null ) {
				passwordField.setText(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void saveData() {
		BufferedWriter write = null;
		try{
			File outfile = new File("D:\\search.ini");
			write = new BufferedWriter(new FileWriter(outfile));
			write.write(field.getText());
			write.newLine();
			write.flush();
			write.write(addressField.getText());
			write.newLine();
			write.flush();
			write.write(portField.getText());
			write.newLine();
			write.flush();
			write.write(userField.getText());
			write.newLine();
			write.flush();
			write.write(passwordField.getPassword());
			write.newLine();
			write.flush();
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			try {
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
