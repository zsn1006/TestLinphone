package org.linphone.newphone.db;

import java.util.ArrayList;

import org.linphone.newphone.bean.Contact;
import org.linphone.newphone.bean.UserInfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

public class LoginRecordsDao {
	public static final String TABLE = "db_loginRecords";
	private static LoginRecordsDao instance = null;

	private LoginRecordsDao() {
	}

	public static LoginRecordsDao getInstance() {
		if (instance == null) {
			load();
		}
		return instance;
	}

	/**
	 * 双重检查锁模式
	 * 
	 * @since 从最初版本添加。
	 */
	private static synchronized void load() {
		if (instance == null) {
			instance = new LoginRecordsDao();
		}
	}

	/**
	 * 根据账号删除所有消息
	 * 
	 * @param account
	 *            通讯录账号
	 */
	public void deleteAll(String account) {
		DBHelper.getWriteableDB().execSQL("delete from " + TABLE + " where account=" + account);
	}

	/**
	 * 根据ID删除数据库单挑消息
	 * 
	 * @param id
	 *            数据库自动生成的ID
	 */
	public void deleteById(int id) {
		DBHelper.getWriteableDB().execSQL("delete from " + TABLE + " where _id=" + id);
	}

	/**
	 * 将单条消息插入数据库
	 * 
	 * @param contact
	 */
	public void insertContacts(UserInfo user) {
		String sql = "insert into " + TABLE
				+ " (name,account,icon,password,ip,port,proxy,initial) values (?,?,?,?,?,?,?,?)";
		SQLiteStatement stat = DBHelper.getWriteableDB().compileStatement(sql);
		DBHelper.getWriteableDB().beginTransaction();
		stat.bindString(1, user.getName());
		stat.bindString(2, user.getAccount());
		stat.bindString(3, user.getIcon());
		stat.bindString(4, user.getPassword());
		stat.bindString(5, user.getIp());
		stat.bindString(6, user.getPort());
		stat.bindString(7, user.getProxy());
		stat.bindString(8, user.getInitial());
		stat.executeInsert();
		DBHelper.getWriteableDB().setTransactionSuccessful();
		DBHelper.getWriteableDB().endTransaction();
	}

	/**
	 * 将消息数组插入数据库
	 * 
	 * @param contacts
	 *            消息数组
	 */
	public void insertContacts(ArrayList<UserInfo> users) {
		String sql = "insert into " + TABLE
				+ " (name,account,icon,password,ip,port,proxy,initial) values (?,?,?,?,?,?,?,?)";
		SQLiteStatement stat = DBHelper.getWriteableDB().compileStatement(sql);
		DBHelper.getWriteableDB().beginTransaction();
		for (UserInfo user : users) {
			stat.bindString(1, user.getName());
			stat.bindString(2, user.getAccount());
			stat.bindString(3, user.getIcon());
			stat.bindString(4, user.getPassword());
			stat.bindString(5, user.getIp());
			stat.bindString(6, user.getPort());
			stat.bindString(7, user.getProxy());
			stat.bindString(8, user.getInitial());
			stat.executeInsert();
		}
		DBHelper.getWriteableDB().setTransactionSuccessful();
		DBHelper.getWriteableDB().endTransaction();
	}

	/**
	 * 从数据库读取消息 ，返回消息列表
	 * 
	 * @param account
	 *            账号
	 * @return
	 */
	public UserInfo readContacts(String account) {

		UserInfo user = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.getReadableDB().rawQuery(
					"select * from " + TABLE + " where account=?", new String[] { account });

			if (cursor.moveToFirst()) {
				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String zhanghao = cursor.getString(cursor.getColumnIndex("account"));
				String icon = cursor.getString(cursor.getColumnIndex("icon"));
				String password = cursor.getString(cursor.getColumnIndex("password"));
				String ip = cursor.getString(cursor.getColumnIndex("ip"));
				String port = cursor.getString(cursor.getColumnIndex("port"));
				String proxy = cursor.getString(cursor.getColumnIndex("proxy"));
				String initial = cursor.getString(cursor.getColumnIndex("initial"));

				user = new UserInfo(id, name, zhanghao, icon, password, ip, port, proxy, initial);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return user;
	}

	/**
	 * 从数据库读取消息 ，返回消息列表
	 * 
	 * @param account
	 *            账号
	 * @return
	 */
	public ArrayList<UserInfo> readContacts() {
		ArrayList<UserInfo> list = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.getReadableDB().rawQuery("select * from " + TABLE + " order by _id desc", null);
			list = new ArrayList<UserInfo>();
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String zhanghao = cursor.getString(cursor.getColumnIndex("account"));
				String icon = cursor.getString(cursor.getColumnIndex("icon"));
				String password = cursor.getString(cursor.getColumnIndex("password"));
				String ip = cursor.getString(cursor.getColumnIndex("ip"));
				String port = cursor.getString(cursor.getColumnIndex("port"));
				String proxy = cursor.getString(cursor.getColumnIndex("proxy"));
				String initial = cursor.getString(cursor.getColumnIndex("initial"));

				UserInfo user = new UserInfo(id, name, zhanghao, icon, password, ip, port, proxy,
						initial);
				list.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * 修改信息状态，是否阅读
	 * 
	 * @param id
	 */
	public int modifyMessageStatus(Contact contact, String account) {

		try {
			// DBHelper.getWriteableDB().beginTransaction();
			// String sql = "UPDATE " + TABLE + " set status='1' where _id=" +
			// id;
			// DBHelper.getWriteableDB().execSQL(sql);
			// DBHelper.getWriteableDB().setTransactionSuccessful();
			// DBHelper.getWriteableDB().endTransaction();

			ContentValues cv = new ContentValues();
			cv.put("name", contact.getName());
			cv.put("account", contact.getAccount());
			cv.put("icon", contact.getSipAddr());
			cv.put("password", contact.getIcon());
			cv.put("ip", contact.getCreateTime());
			cv.put("port", contact.getAddress());
			cv.put("proxy", contact.getPhone());
			cv.put("initial", contact.getInitial());
			String[] args = { String.valueOf(account) };
			return DBHelper.getWriteableDB().update(TABLE, cv, "account=?", args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
