package org.linphone.newphone.db;

import java.util.ArrayList;

import org.linphone.newphone.bean.CallRecords;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

public class CallRecordsDao {
	public static final String TABLE = "db_callRecords";
	private static CallRecordsDao instance = null;

	private CallRecordsDao() {
	}

	public static CallRecordsDao getInstance() {
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
			instance = new CallRecordsDao();
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
	
	public void deleteAll() {
		DBHelper.getWriteableDB().execSQL("delete from " + TABLE);
	}

	/**
	 * 将单条消息插入数据库
	 * 
	 * @param contact
	 */
	public void insertCallRedords(CallRecords call) {
		String sql = "insert into " + TABLE
				+ " (account,starttime,timelength,calltype,incoming) values (?,?,?,?,?)";
		SQLiteStatement stat = DBHelper.getWriteableDB().compileStatement(sql);
		DBHelper.getWriteableDB().beginTransaction();
		stat.bindString(1, call.getAccount());
		stat.bindString(2, call.getStartTime());
		stat.bindString(3, String.valueOf(call.getTimeLength()));
		stat.bindString(4, String.valueOf(call.getCallType()));
		stat.bindString(5, String.valueOf(call.getIncoming()));
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
	public void insertCallRedords(ArrayList<CallRecords> calls) {
		String sql = "insert into " + TABLE
				+ " (account,starttime,timelength,calltype,incoming) values (?,?,?,?,?)";
		SQLiteStatement stat = DBHelper.getWriteableDB().compileStatement(sql);
		DBHelper.getWriteableDB().beginTransaction();
		for (CallRecords call : calls) {
			stat.bindString(1, call.getAccount());
			stat.bindString(2, call.getStartTime());
			stat.bindString(3, String.valueOf(call.getTimeLength()));
			stat.bindString(4, String.valueOf(call.getCallType()));
			stat.bindString(5, String.valueOf(call.getIncoming()));
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
	public CallRecords readCallRedords(String account) {

		CallRecords call = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.getReadableDB().rawQuery(
					"select * from " + TABLE + " where account=?", new String[] { account });

			if (cursor.moveToFirst()) {
				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String zhanghao = cursor.getString(cursor.getColumnIndex("account"));
				String starttime = cursor.getString(cursor.getColumnIndex("starttime"));
				int timelength = cursor.getInt(cursor.getColumnIndex("timelength"));
				int calltype = cursor.getInt(cursor.getColumnIndex("calltype"));
				int incoming = cursor.getInt(cursor.getColumnIndex("incoming"));

				call = new CallRecords(id, zhanghao, starttime, timelength, calltype, incoming);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return call;
	}

	/**
	 * 从数据库读取消息 ，返回消息列表
	 * 
	 * @param account
	 *            账号
	 * @return
	 */
	public ArrayList<CallRecords> readCallRedords() {
		ArrayList<CallRecords> list = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.getReadableDB().rawQuery("select * from " + TABLE + " order by _id desc", null);
			list = new ArrayList<CallRecords>();
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {

				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String zhanghao = cursor.getString(cursor.getColumnIndex("account"));
				String starttime = cursor.getString(cursor.getColumnIndex("starttime"));
				int timelength = cursor.getInt(cursor.getColumnIndex("timelength"));
				int calltype = cursor.getInt(cursor.getColumnIndex("calltype"));
				int incoming = cursor.getInt(cursor.getColumnIndex("incoming"));

				CallRecords call = new CallRecords(id, zhanghao, starttime, timelength, calltype,
						incoming);
				list.add(call);
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

}
