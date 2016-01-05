package org.linphone.newphone.db;

import org.linphone.newphone.tools.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper {

	/** 数据库版本 */
	private static final int VERSION = 2;
	private static DBHelper helper = null;
	private static SQLiteDatabase readableDB = null;
	private static SQLiteDatabase writeableDB = null;

	/**
	 * 构造方法。
	 * 
	 * @param context
	 *            应用的上下文
	 * @param name
	 *            数据库名称
	 * @param factory
	 *            CursorFactory
	 * @param version
	 *            数据库版本
	 * @see CursorFactory
	 * @see DBHelper#DBHelper(Context, String, int)
	 * @since 从最初版本添加。
	 */
	private DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 构造方法。
	 * 
	 * @param context
	 *            应用的上下文
	 * @param name
	 *            数据库名称
	 * @param version
	 *            数据库版本
	 * @see DBHelper#DBHelper(Context, String)
	 * @since 从最初版本添加。
	 */
	private DBHelper(Context context, String name, int version) {
		super(context, name, null, VERSION);
	}

	/**
	 * 构造方法。
	 * 
	 * @param context
	 *            应用的上下文
	 * @param name
	 *            数据库名称
	 * @since 从最初版本添加。
	 */
	private DBHelper(Context context, String name) {
		this(context, name, VERSION);
		LogUtil.log("DB DBHelper");
	}

	/**
	 * 获取数据库读操作对象。
	 * 
	 * @return SQLiteDatabase
	 * @see SQLiteDatabase
	 * @since 从最初版本添加。
	 */
	public static SQLiteDatabase getReadableDB() {
		return readableDB;
	}

	/**
	 * 获取数据库写操作对象。
	 * 
	 * @return SQLiteDatabase
	 * @see SQLiteDatabase
	 * @since 从最初版本添加。
	 */
	public static SQLiteDatabase getWriteableDB() {
		return writeableDB;
	}

	/**
	 * 初始载入数据库信息。
	 * 
	 * @param context
	 *            应用的上下文
	 * @since 从最初版本添加。
	 */
	public static void load(Context context) {
		LogUtil.log("DB load");
		DBHelper instance = helper;
		if (instance == null) {
			init(context);
		}
	}

	/**
	 * 双重检查锁模式加载数据库。
	 * 
	 * @see DBHelper#load()
	 * @since 从最初版本添加。
	 */
	private static synchronized void init(Context context) {
		LogUtil.log("DB init");
		DBHelper instance = helper;
		if (instance == null) {
			helper = new DBHelper(context, context.getPackageName());
			readableDB = helper.getReadableDatabase();
			writeableDB = helper.getWritableDatabase();
		}
	}

	/**
	 * 卸载数据库。
	 * 
	 * @since 从最初版本添加。
	 */
	public static void unload() {
		DBHelper instance = helper;
		if (instance == null)
			return;
		release();
	}

	/**
	 * 双重检查锁模式卸载数据库。
	 * 
	 * @see DBHelper#unload()
	 * @since 从最初版本添加。
	 */
	private static synchronized void release() {
		DBHelper instance = helper;
		if (instance != null) {
			helper.close();
			helper = null;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.log("DB onCreate");
		String sql_XT_USER = "CREATE TABLE "
				+ ContactsDao.TABLE
				+ "(_id integer primary key autoincrement,name varchar(16) NULL,account varchar(16) NULL,sipaddr varchar(64) NULL,icon varchar(32) NULL,createtime datetime NULL,address varchar(64) NULL,phone varchar(12) NULL,initial varchar(8) NULL)";
		db.execSQL(sql_XT_USER);
		
		sql_XT_USER = "CREATE TABLE "
				+ LoginRecordsDao.TABLE
				+ "(_id integer primary key autoincrement,name varchar(16) NULL,account varchar(16) NULL,icon varchar(32) NULL,password varchar(32) NULL,ip varchar(32) NULL,port varchar(8) NULL,proxy varchar(64) NULL,initial varchar(8) NULL)";
		db.execSQL(sql_XT_USER);
		
		sql_XT_USER = "CREATE TABLE "
				+ CallRecordsDao.TABLE
				+ "(_id integer primary key autoincrement,account varchar(16) NULL,starttime datetime NULL,timelength varchar(4) NULL,calltype varchar(4) NULL,incoming varchar(4) NULL)";
		db.execSQL(sql_XT_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {
			String sql = "alter table [" + ContactsDao.TABLE + "] add [jump] varchar(4)";
			db.execSQL(sql);
		}
	}
}
