import java.sql.*;

public class dbConnClass {
	private String sql = "";
	private String ip = "";
	private String port = "";
	private String dbName = "";
	private String user = "";
	private String password = "";
	private static Connection conn = null;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param sql 要执行的sql语句
	 * @param ip 数据库ip
	 * @param port 连接端口
	 * @param dbName 数据库名称
	 * @param user 数据库账号
	 * @param password 数据库密码
	 */
	public dbConnClass(String sql, String ip, String port, String dbName,
			String user, String password) {
		super();
		this.sql = sql;
		this.ip = ip;
		this.port = port;
		this.dbName = dbName;
		this.user = user;
		this.password = password;
	}
	
	/**
	 * @param ip 数据库ip
	 * @param port 连接端口
	 * @param dbName 数据库名称
	 * @param user 数据库账号
	 * @param password 数据库密码
	 */
	public dbConnClass(String ip, String port, String dbName, String user,
			String password) {
		super();
		this.ip = ip;
		this.port = port;
		this.dbName = dbName;
		this.user = user;
		this.password = password;
	}

	public dbConnClass() {
		super();
	}
	
	public boolean connectDB() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://" + this.ip + ":" + this.port + "/" + this.dbName;
		try {
		     // 加载驱动程序
		     Class.forName(driver);
		     // 连续数据库
		     conn = DriverManager.getConnection(url, this.user, this.password);
		     return conn.isClosed();
		} catch(ClassNotFoundException e) {   
	    	 System.out.println("Sorry,can`t find the Driver!");   
	    	 e.printStackTrace();   
	    	 return false;
    	} catch(SQLException e) {   
    		 e.printStackTrace();  
    		 return false;
    	} catch(Exception e) {   
    		 e.printStackTrace();   
    		 return false;
    	}   
	}
	
	public int selectDB() throws SQLException {
		Statement statement = this.conn.createStatement();
		ResultSet rs = statement.executeQuery(this.sql);
		if(rs.last()) {
			return rs.getRow();
		} else {
			return 0;
		}
	}
	
	public int selectDB(String sql) throws SQLException {
		this.sql = sql;
		Statement statement = this.conn.createStatement();
		ResultSet rs = statement.executeQuery(this.sql);
		if(rs.last()) {
			return rs.getRow();
		} else {
			return 0;
		}
	}
	
	public ResultSet selectDBResult() throws SQLException {
		this.sql = sql;
		Statement statement = this.conn.createStatement();
		ResultSet rs = statement.executeQuery(this.sql);
		return rs;
	}
	
	public ResultSet selectDBResult(String sql) throws SQLException {
		this.sql = sql;
		Statement statement = this.conn.createStatement();
		ResultSet rs = statement.executeQuery(this.sql);
		if(rs.last()) {
			return rs;
		} else {
			return rs;
		}
		
	}
	
	public int updateDB() throws SQLException {
		Statement statement = this.conn.createStatement();
		return statement.executeUpdate(sql);
	}
	
	
	
	public int updateDB(String sql) throws SQLException {
		this.sql = sql;
		Statement statement = this.conn.createStatement();
		return statement.executeUpdate(sql);
	}
}
