import java.io.*;
import java.util.*;

/**
 * 通过路径或直接输入关键词保存关键词集合
 * @author Administrator
 *
 */
public class ReadFileClass {
	private ArrayList keyWords = new ArrayList();
	private String filePath = "";
	private HashMap keyWordValue = new HashMap();
	/**
	 * 构造函数  保存路径到filePath 并通过路径取得文件  解析出关键词  放入keyWords
	 * @param filePath 路径字串
	 */
	public ReadFileClass(String filePath) {
		super();
		this.filePath = filePath;
		BufferedReader reader = null;
		try {
			FileReader file = new FileReader(this.filePath);
			reader = new BufferedReader(file);
			String str = "";
			while ( (str = reader.readLine()) != null ) {
				if(str == "") {
					continue;
				}
				this.keyWords.add(str);
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
	/**
	 * 构造函数  若为单一关键词 则保存关键词 路径不保存
	 * @param filePath 只为方便函数重载   不会保存
	 * @param ketWord 单一关键词  保存进keyWords
	 */
	public ReadFileClass(String keyWord, String filePath) {
		super();
		this.keyWords.add(keyWord);
	}
	/**
	 * 空构造函数
	 */
	public ReadFileClass() {
		super();
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public ArrayList getKeyWords() {
		return keyWords;
	}
	public HashMap getKeyWordValue() {
		return keyWordValue;
	}
	public void setKeyWords(ArrayList keyWords) {
		this.keyWords = keyWords;
	}
	
	/**
	 * 把关键词--获得链接个数加入hashmap
	 * @param keyWord
	 * @param value
	 */
	public void addKeyWordValue(String keyWord, int value) {
		keyWordValue.put(keyWord, value);
	}
}
