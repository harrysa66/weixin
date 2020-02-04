package com.weixin.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import java.sql.*;

/**
 * 数据库连接类
 * 说明:封装了 无参，有参，存储过程的调用
 *
 * @author iflytek
 */
public class ConnectionDB {

    /**
     * SSH连接信息
     */
    private static final String host = "jbossews-harrysa66.rhcloud.com";
    private static final String user = "558ce6524382ec561000007d";
    private static final String password = "";
    private static final int port = 22;
    private static final int maxWaitTime = 10 * 1000;
    private static final String keyfile = "E:/keys/openshift/harrysa66-20150626";
    private static final String passphrase = "";
    private static final boolean sshKey = true;

    static int lport = 12345;//本地端口  
    static String rhost = "127.3.198.130";//远程MySQL服务器  
    static int rport = 3306;//远程MySQL服务端口  

    /**
     * 数据库驱动类名称
     */
//    private static final String DRIVER = "com.mysql.jdbc.Driver";

    /**
     * 连接字符串
     */
    //private static final String URLSTR = "jdbc:mysql://localhost:3306/weixin";
    //private static final String URLSTR = "jdbc:mysql://127.3.198.130:3306/jbossews";
    //private static final String URLSTR_SSH = "jdbc:mysql://127.0.0.1:"+lport+"/jbossews";  
    //private static final String URLSTR_LO = "jdbc:mysql://localhost:3306/weixin";

    /**
     * 用户名
     */
    //private static final String USERNAME = "root";
    //private static final String USERNAME = "adminMYP82TU";
    //private static final String USERNAME_LO = "root";

    /**
     * 密码
     */
    //private static final String USERPASSWORD = "ZXXcc123";
    //private static final String USERPASSWORD = "9xVTxI-J97Bc";
    //private static final String USERPASSWORD_LO = "12323211";//12323211,123456

    /**
     * 创建数据库连接对象
     */
    private Connection connnection = null;

    /**
     * 创建PreparedStatement对象
     */
    private PreparedStatement preparedStatement = null;

    /**
     * 创建CallableStatement对象
     */
    private CallableStatement callableStatement = null;

    /**
     * 创建结果集对象
     */
    private ResultSet resultSet = null;

    private static Session session = null;

    /**
     * 建立数据库连接
     *
     * @return 数据库连接
     */
    public Connection getConnection() {
        try {
            String driver = ConfigManager.getInstance().getString("driver");
            String url = ConfigManager.getInstance().getString("url");
            String username = ConfigManager.getInstance().getString("username");
            String password = ConfigManager.getInstance().getString("password");
            // 加载数据库驱动程序
            Class.forName(driver);
            // 获取连接  
            connnection = DriverManager.getConnection(url, username, password);
            //connnection = DriverManager.getConnection(URLSTR,USERNAME,USERPASSWORD);  
            //connnection = DriverManager.getConnection(URLSTR_LO,USERNAME_LO,USERPASSWORD_LO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return connnection;
    }

    /**
     * 建立SSH数据库连接
     *
     * @return 数据库连接
     */
    public void getSSHConnection() {
        try {
            JSchChannel client = new JSchChannel(host, user, port, maxWaitTime,
                    keyfile, passphrase);
            ChannelSftp sftp = client.open();
            Session session = sftp.getSession();
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 关闭所有资源
     */
    public void closeAll() {
        // 关闭结果集对象  
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // 关闭PreparedStatement对象  
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // 关闭CallableStatement 对象  
        if (callableStatement != null) {
            try {
                callableStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // 关闭Connection 对象  
        if (connnection != null) {
            try {
                connnection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        ConnectionDB db = new ConnectionDB();
        db.getConnection();
    }

}  
