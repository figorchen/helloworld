import java.io.*;
import java.util.*;

/**
 * ͨ��·����ֱ������ؼ��ʱ���ؼ��ʼ���
 * @author Administrator
 *
 */
public class ReadFileClass {
	private ArrayList keyWords = new ArrayList();
	private String filePath = "";
	private HashMap keyWordValue = new HashMap();
	/**
	 * ���캯��  ����·����filePath ��ͨ��·��ȡ���ļ�  �������ؼ���  ����keyWords
	 * @param filePath ·���ִ�
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
	 * ���캯��  ��Ϊ��һ�ؼ��� �򱣴�ؼ��� ·��������
	 * @param filePath ֻΪ���㺯������   ���ᱣ��
	 * @param ketWord ��һ�ؼ���  �����keyWords
	 */
	public ReadFileClass(String keyWord, String filePath) {
		super();
		this.keyWords.add(keyWord);
	}
	/**
	 * �չ��캯��
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
	 * �ѹؼ���--������Ӹ�������hashmap
	 * @param keyWord
	 * @param value
	 */
	public void addKeyWordValue(String keyWord, int value) {
		keyWordValue.put(keyWord, value);
	}
}
