package com.weixin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class TestJSch {

	static int lport = 3306;//本地端口
	static String rhost = "127.3.198.130";//远程MySQL服务器
	static int rport = 3306;//远程MySQL服务端口
	
	private static final String keyfile = "E:/key/harrysa66-20150626"; 
    private static final String passphrase = ""; 
    private static final boolean sshKey = true;

	public static void go() {
		String user = "558ce6524382ec561000007d";//SSH连接用户名
		//String password = "";//SSH连接密码
		String host = "107.22.95.70";//SSH服务器
		int port = 22;//SSH访问端口
		try {
			JSch jsch = new JSch();
			if (sshKey && keyfile != null && keyfile.length() > 0) { 
				jsch.addIdentity(keyfile, passphrase); 
	        } 
			Session session = jsch.getSession(user, host, port);
			//session.setPassword(password);
			//session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			System.out.println(session.getServerVersion());//这里打印SSH服务器版本信息
			int assinged_port = session.setPortForwardingL(lport, rhost, rport);
			System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sql() {
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
			st = conn.createStatement();
			String sql = "SELECT COUNT(1) FROM All";
			rs = st.executeQuery(sql);
			while (rs.next())
				System.out.println(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//rs.close();st.close();conn.close();
		}
	}

	public static void main(String[] args) {
		go();
		//sql();
	}
}