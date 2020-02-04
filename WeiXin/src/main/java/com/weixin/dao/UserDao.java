package com.weixin.dao;

import com.weixin.constant.FlagConstant;
import com.weixin.po.User;
import com.weixin.util.DateUtil;

public class UserDao extends BaseDao{
	
	/**
	 * 得到用户
	 * @param username
	 * @return
	 */
	public User getUser(String username){
		Object[] params = new Object[1];
		params[0] = username;
		String sql = "select id,username,weixin_id as weixinId,createtime,flag,`status` from weixin_user where username=?";
		User user = (User)executeQueryObject(sql, User.class, params);
		return user;
	}
	
	/**
	 * 创建用户
	 * @param username
	 * @param weixinId
	 * @return
	 */
	public int insertUser(String username,String weixinId){
		Object[] params = new Object[5];
		params[0] = username;
		params[1] = weixinId;
		params[2] = DateUtil.getCurrentDate();
		params[3] = FlagConstant.INIT;
		params[4] = FlagConstant.STATUS_0;
		String sql = "insert into weixin_user(username,weixin_id,createtime,flag,status) values(?,?,?,?,?)";
		int line = executeUpdate(sql, params);
		return line;
	}
	
	/**
	 * 设置标识
	 * @param username
	 * @param flag
	 * @return
	 */
	public int setFlag(String username,String flag){
		Object[] params = new Object[2];
		params[0] = flag;
		params[1] = username;
		String sql = "update weixin_user set flag=? where username=?";
		int line = executeUpdate(sql, params);
		return line;
	}
	
	/**
	 * 删除用户
	 * @param username
	 * @return
	 */
	public int deleteUser(String username){
		Object[] params = new Object[1];
		params[0] = username;
		String sql = "delete from weixin_user where username=?";
		int line = executeUpdate(sql, params);
		return line;
	}
	
	public static void main(String[] args) {
		UserDao userDao = new UserDao();
		User user = userDao.getUser("oldoPuHWQwhky5mtvuS8LXKDP-NE");
		System.out.println(user.getUsername());
	}

}
