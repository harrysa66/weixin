package com.weixin.dao;

import com.weixin.util.ConnectionDB;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDao {
	
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
    
    ConnectionDB db = new ConnectionDB();
	
	/** 
     * insert update delete SQL语句的执行的统一方法 
     * @param sql SQL语句 
     * @param params 参数数组，若没有参数则为null 
     * @return 受影响的行数 
     */  
    public int executeUpdate(String sql, Object[] params) {  
        // 受影响的行数  
        int affectedLine = 0;  
          
        try {  
            // 获得连接  
            connnection = db.getConnection();  
            // 调用SQL   
            preparedStatement = connnection.prepareStatement(sql);  
              
            // 参数赋值  
            if (params != null) {  
                for (int i = 0; i < params.length; i++) {  
                    preparedStatement.setObject(i + 1, params[i]);  
                }  
            }  
              
            // 执行  
            affectedLine = preparedStatement.executeUpdate();  
  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } finally {  
            // 释放资源  
        	db.closeAll();  
        }  
        return affectedLine;  
    }  
  
    /** 
     * SQL 查询将查询结果直接放入ResultSet中 
     * @param sql SQL语句 
     * @param params 参数数组，若没有参数则为null 
     * @return 结果集 
     */  
    private ResultSet executeQueryRS(String sql, Object[] params) {  
        try {  
            // 获得连接  
            connnection = db.getConnection();  
              
            // 调用SQL  
            preparedStatement = connnection.prepareStatement(sql);  
              
            // 参数赋值  
            if (params != null) {  
                for (int i = 0; i < params.length; i++) {  
                    preparedStatement.setObject(i + 1, params[i]);  
                }  
            }  
              
            // 执行  
            resultSet = preparedStatement.executeQuery();  
  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }   
  
        return resultSet;  
    }  
      
    /** 
     * SQL 查询将查询结果：一行一列 
     * @param sql SQL语句 
     * @param params 参数数组，若没有参数则为null 
     * @return 结果集 
     */  
    public Object executeQuerySingle(String sql, Object[] params) {  
        Object object = null;  
        try {  
            // 获得连接  
            connnection = db.getConnection();  
              
            // 调用SQL  
            preparedStatement = connnection.prepareStatement(sql);  
              
            // 参数赋值  
            if (params != null) {  
                for (int i = 0; i < params.length; i++) {  
                    preparedStatement.setObject(i + 1, params[i]);  
                }  
            }  
              
            // 执行  
            resultSet = preparedStatement.executeQuery();  
  
            if(resultSet.next()) {  
                object = resultSet.getObject(1);  
            }  
              
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } finally {  
        	db.closeAll();  
        }  
  
        return object;  
    }  
    
    /** 
     * SQL 查询将查询结果：单个
     * @param sql SQL语句 
     * @param clazz
     * @return 结果集 
     */  
    public Object executeQueryObject(String sql, Class clazz,Object[] params) {  
        Object object = null;  
        Field[] fields = clazz.getDeclaredFields();
        // 执行SQL获得结果集  
        ResultSet rs = executeQueryRS(sql, params);  
        try {  
            if(rs.next() && rs.getRow() == 1){
            	object = clazz.newInstance();
            	for(Field field : fields){
            		field.setAccessible(true);
            		field.set(object, rs.getObject(field.getName()));
            	}
            }
        } catch (Exception e) {
            object = null;
            System.out.println(e.getMessage());  
        } finally {  
            // 关闭所有资源  
        	db.closeAll();  
        }  
        return object;  
    }
  
    /** 
     * 获取结果集，并将结果放在List中 
     *  
     * @param sql 
     *            SQL语句 
     * @return List 
     *                       结果集 
     */  
    public List executeQuery(String sql, Class clazz, Object[] params) {
        // 执行SQL获得结果集  
        ResultSet rs = executeQueryRS(sql, params);  
        // 创建List  
        List list = new ArrayList();  
        Object object = null;  
        Field[] fields = clazz.getDeclaredFields();
        try {  
            // 将ResultSet的结果保存到List中  
            while (rs.next()) {  
            	object = clazz.newInstance();
            	for(Field field : fields){
            		field.setAccessible(true);
            		field.set(object, rs.getObject(field.getName()));
            	}
                list.add(object);  
            }  
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        } finally {  
            // 关闭所有资源  
        	db.closeAll();  
        }  
        return list;  
    }  
      
    /** 
     * 存储过程带有一个输出参数的方法 
     * @param sql 存储过程语句 
     * @param params 参数数组 
     * @param outParamPos 输出参数位置 
     * @param SqlType 输出参数类型 
     * @return 输出参数的值 
     */  
    public Object executeQuery(String sql, Object[] params,int outParamPos, int SqlType) {
        Object object = null;  
        connnection = db.getConnection();  
        try {  
            // 调用存储过程  
            callableStatement = connnection.prepareCall(sql);  
              
            // 给参数赋值  
            if(params != null) {  
                for(int i = 0; i < params.length; i++) {  
                    callableStatement.setObject(i + 1, params[i]);  
                }  
            }  
              
            // 注册输出参数  
            callableStatement.registerOutParameter(outParamPos, SqlType);  
              
            // 执行  
            callableStatement.execute();  
              
            // 得到输出参数  
            object = callableStatement.getObject(outParamPos);  
              
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } finally {  
            // 释放资源  
        	db.closeAll();  
        }  
          
        return object;  
    }
    
    public static void main(String[] args) {
    	BaseDao baseDao = new BaseDao();
    	Long count = (Long) baseDao.executeQuerySingle("select count(*) from weixin_user", null);
    	//User user = (User)baseDao.executeQueryObject("select id,username,weixin_id as weixinId,createtime,flag,`status` from weixin_user", User.class,null);
    	//List<User> list = baseDao.excuteQuery("select id,username,weixin_id as weixinId,createtime,flag,`status` from weixin_user", User.class, null);
    	System.out.println(count);
    	//System.out.println(user.getUsername());
    	//System.out.println(list.size());
    }

}
