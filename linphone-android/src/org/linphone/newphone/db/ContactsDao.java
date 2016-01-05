package org.linphone.newphone.db;

import java.util.ArrayList;

import org.linphone.newphone.bean.Contact;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

public class ContactsDao {
	public static final String TABLE = "db_contacts";
	private static ContactsDao instance = null;

	private ContactsDao() {
	}

	public static ContactsDao getInstance() {
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
			instance = new ContactsDao();
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
	public void insertContacts(Contact contact) {
		String sql = "insert into "
				+ TABLE
				+ " (name,account,sipaddr,icon,createtime,address,phone,initial) values (?,?,?,?,?,?,?,?)";
		SQLiteStatement stat = DBHelper.getWriteableDB().compileStatement(sql);
		DBHelper.getWriteableDB().beginTransaction();
		stat.bindString(1, contact.getName());
		stat.bindString(2, contact.getAccount());
		stat.bindString(3, contact.getSipAddr());
		stat.bindString(4, contact.getIcon());
		stat.bindString(5, contact.getCreateTime());
		stat.bindString(6, contact.getAddress());
		stat.bindString(7, contact.getPhone());
		stat.bindString(8, contact.getInitial());
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
	public void insertContacts(ArrayList<Contact> contacts) {
		String sql = "insert into "
				+ TABLE
				+ " (name,account,sipaddr,icon,createtime,address,phone,initial) values (?,?,?,?,?,?,?,?)";
		SQLiteStatement stat = DBHelper.getWriteableDB().compileStatement(sql);
		DBHelper.getWriteableDB().beginTransaction();
		for (Contact contact : contacts) {
			stat.bindString(1, contact.getName());
			stat.bindString(2, contact.getAccount());
			stat.bindString(3, contact.getSipAddr());
			stat.bindString(4, contact.getIcon());
			stat.bindString(5, contact.getCreateTime());
			stat.bindString(6, contact.getAddress());
			stat.bindString(7, contact.getPhone());
			stat.bindString(8, contact.getInitial());
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
	public Contact readContacts(String account) {
		Contact contact = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.getReadableDB().rawQuery(
					"select * from " + TABLE + " where account=?", new String[] { account });

			if (cursor.moveToFirst()) {
				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String zhanghao = cursor.getString(cursor.getColumnIndex("account"));
				String sipaddr = cursor.getString(cursor.getColumnIndex("sipaddr"));
				String icon = cursor.getString(cursor.getColumnIndex("icon"));
				String createtime = cursor.getString(cursor.getColumnIndex("createtime"));
				String address = cursor.getString(cursor.getColumnIndex("address"));
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				String initial = cursor.getString(cursor.getColumnIndex("initial"));

				contact = new Contact(id, name, zhanghao, sipaddr, icon, createtime, address, phone,
						initial);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return contact;
	}

	/**
	 * 从数据库读取消息 ，返回消息列表
	 * 
	 * @param account
	 *            账号
	 * @return
	 */
	public ArrayList<Contact> readContacts() {
		ArrayList<Contact> list = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.getReadableDB().rawQuery("select * from " + TABLE, null);
			list = new ArrayList<Contact>();
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String zhanghao = cursor.getString(cursor.getColumnIndex("account"));
				String sipaddr = cursor.getString(cursor.getColumnIndex("sipaddr"));
				String icon = cursor.getString(cursor.getColumnIndex("icon"));
				String createtime = cursor.getString(cursor.getColumnIndex("createtime"));
				String address = cursor.getString(cursor.getColumnIndex("address"));
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				String initial = cursor.getString(cursor.getColumnIndex("initial"));

				Contact contact = new Contact(id, name, zhanghao, sipaddr, icon, createtime,
						address, phone, initial);
				list.add(contact);
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
			cv.put("sipaddr", contact.getSipAddr());
			cv.put("icon", contact.getIcon());
			cv.put("createtime", contact.getCreateTime());
			cv.put("address", contact.getAddress());
			cv.put("phone", contact.getPhone());
			cv.put("initial", contact.getInitial());
			String[] args = { String.valueOf(account) };
			return DBHelper.getWriteableDB().update(TABLE, cv, "account=?", args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
