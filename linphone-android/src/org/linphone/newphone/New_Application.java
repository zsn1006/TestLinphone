package org.linphone.newphone;

import java.util.ArrayList;

import org.linphone.newphone.bean.CallRecords;
import org.linphone.newphone.bean.Contact;
import org.linphone.newphone.bean.ContactSort;
import org.linphone.newphone.bean.UserInfo;
import org.linphone.newphone.db.CallRecordsDao;
import org.linphone.newphone.db.ContactsDao;
import org.linphone.newphone.db.DBHelper;
import org.linphone.newphone.db.LoginRecordsDao;
import org.linphone.newphone.tools.Constants;

import android.app.Application;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author Win
 */
public class New_Application extends Application {

	private final static String mIndexLetter[] = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private static New_Application mInstance;
	private ArrayList<ContactSort> mContactArray;
	private ArrayList<UserInfo> mUserArray;
	private ArrayList<CallRecords> mCallArray;
	
	public static Boolean isNewPhone = true;
	public static Boolean isVideoPhone = false;
	public static Boolean isIncoming = false;
	public static long LongStartTime;
	public static int videoWidth = 9;
	public static int videoHeight = 16;

	public static New_Application getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		DBHelper.load(this);
		mInstance = this;

//		 testContacts();
		readContacts();
//		 testUserInfo();
		readLoginRecords();
//		 testCallRecords();
		readCallRecords();
	}

	private void testContacts() {

		ArrayList<Contact> contacts = new ArrayList<Contact>();

		Contact contact1 = new Contact(1, "张三", "111", Constants.ip, "/aaa.png",
				"2015-10-23 01:12:32", "湖北武汉", "13800000001", "ZS");
		Contact contact2 = new Contact(1, "李四", "222", Constants.ip, "/bbb.png",
				"2015-10-24 02:12:32", "湖北武汉", "13800000002", "LS");
		Contact contact3 = new Contact(1, "王五", "333", Constants.ip, "/ccc.png",
				"2015-10-25 03:12:32", "湖北武汉", "13800000003", "WW");
		Contact contact4 = new Contact(1, "赵六", "444", Constants.ip, "/ddd.png",
				"2015-10-26 04:12:32", "湖北武汉", "13800000004", "ZL");
		Contact contact5 = new Contact(1, "孙七", "555", Constants.ip, "/eee.png",
				"2015-10-27 05:12:32", "湖北武汉", "13800000005", "SQ");
		Contact contact6 = new Contact(1, "胡八", "666", Constants.ip, "/fff.png",
				"2015-10-28 06:12:32", "湖北武汉", "13800000006", "HB");

		contacts.add(contact1);
		contacts.add(contact2);
		contacts.add(contact3);
		contacts.add(contact4);
		contacts.add(contact5);
		contacts.add(contact6);
		ContactsDao.getInstance().insertContacts(contacts);
	}

	private void readContacts() {

		mContactArray = new ArrayList<ContactSort>();
		for (int i = 0; i < mIndexLetter.length; i++) {
			ContactSort cs = new ContactSort();
			cs.setTitle(mIndexLetter[i]);
			cs.setContacts(new ArrayList<Contact>());
			mContactArray.add(cs);
		}

		ArrayList<Contact> contacts = ContactsDao.getInstance().readContacts();
		for (int i = 0; i < contacts.size(); i++) {
			Contact contact = contacts.get(i);
			String letter = contact.getInitial().substring(0, 1);
			int id = Integer.valueOf(letter.charAt(0)) - 'A';

			if (id >= 0 && id < 26) {
				ArrayList<Contact> con = mContactArray.get(id+1).getContacts();
				con.add(contact);
			} else {
				ArrayList<Contact> con = mContactArray.get(0).getContacts();
				con.add(contact);
			}			
		}

		for (int i = mContactArray.size() - 1; i >= 0; i--) {
			ArrayList<Contact> con = mContactArray.get(i).getContacts();
			if (con == null || con.size() <= 0) {
				mContactArray.remove(i);
			}
		}
	}

	public void addNewContact(Contact contact) {

		int count = mContactArray.size();
		String letter = contact.getInitial().substring(0, 1);
		for (int i = 0; i < count; i++) {
			ContactSort sort = mContactArray.get(i);
			if (sort.getTitle().equals(letter)) {
				mContactArray.get(i).getContacts().add(contact);
				return;
			}
		}

		ContactSort cs = new ContactSort();
		cs.setTitle(letter);
		cs.setContacts(new ArrayList<Contact>());
		cs.getContacts().add(contact);
		
		if (count == 0) {
			mContactArray.add(cs);
		} else  if (count == 1) {
			String char0 = mContactArray.get(0).getContacts().get(0).getInitial().substring(0, 1);
			if (char0.compareTo(letter) > 0) {
				mContactArray.add(0, cs);
			} else {
				mContactArray.add(cs);
			}
		} else {
			for (int i = 0; i <= count - 2; i++) {
				String char0 = mContactArray.get(i).getContacts().get(0).getInitial().substring(0, 1);
				String char1 = mContactArray.get(i+1).getContacts().get(0).getInitial().substring(0, 1);
				if (char0.compareTo(letter) > 0 && i == 0) {
					mContactArray.add(0, cs);
				} else if (char1.compareTo(letter) < 0 && i == count - 2) {
					mContactArray.add(count, cs);
				} else if (char0.compareTo(letter) < 0 && char1.compareTo(letter) > 0){
					mContactArray.add(i+1, cs);
				}
			}
		}
	}

	private void testUserInfo() {

		ArrayList<UserInfo> users = new ArrayList<UserInfo>();

		UserInfo user1 = new UserInfo(1, "东方一", "111", "/aaa.png", "123456", Constants.ip,
				"5060", "www.baidu.com", "DFY");
		UserInfo user2 = new UserInfo(1, "西门二", "222", "/bbb.png", "123456", Constants.ip,
				"5070", "www.baidu.com", "XME");
		UserInfo user3 = new UserInfo(1, "南宫三", "333", "/ccc.png", "123456", Constants.ip,
				"5080", "www.baidu.com", "NGS");
		UserInfo user4 = new UserInfo(1, "北堂四", "444", "/ddd.png", "123456", Constants.ip,
				"5090", "www.baidu.com", "BTS");
		UserInfo user5 = new UserInfo(1, "上官五", "555", "/eee.png", "123456", Constants.ip,
				"6060", "www.baidu.com", "SGW");
		UserInfo user6 = new UserInfo(1, "夏侯六", "666", "/fff.png", "123456", Constants.ip,
				"7060", "www.baidu.com", "XHL");

		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
		users.add(user6);
		LoginRecordsDao.getInstance().insertContacts(users);
	}

	private void readLoginRecords() {

		mUserArray = LoginRecordsDao.getInstance().readContacts();
	}

	private void testCallRecords() {

		ArrayList<CallRecords> calls = new ArrayList<CallRecords>();

		CallRecords call1 = new CallRecords(1, "111", "2015-11-12 10:20:30", 30, 1, 1);
		CallRecords call2 = new CallRecords(2, "222", "2015-11-15 08:20:30", 300, 2, 0);
		CallRecords call3 = new CallRecords(3, "333", "2015-11-19 10:20:30", 180, 0, 1);
		CallRecords call4 = new CallRecords(4, "111", "2015-11-12 12:20:30", 20, 1, 0);
		CallRecords call5 = new CallRecords(5, "333", "2015-11-19 16:35:30", 50, 2, 1);
		CallRecords call6 = new CallRecords(6, "111", "2015-11-12 16:20:30", 90, 1, 1);

		calls.add(call1);
		calls.add(call2);
		calls.add(call3);
		calls.add(call4);
		calls.add(call5);
		calls.add(call6);
		CallRecordsDao.getInstance().insertCallRedords(calls);
	}

	private void readCallRecords() {

		mCallArray = CallRecordsDao.getInstance().readCallRedords();

		for (int i = 0; i < mCallArray.size(); i++) {
			String account = mCallArray.get(i).getAccount();
			Contact contact = ContactsDao.getInstance().readContacts(account);
			if (contact != null) {
				mCallArray.get(i).setContact(contact);
			}
		}
	}
	
	public void addNewRecords(CallRecords record) {
		mCallArray.add(0, record);
		
		String account = record.getAccount();
		Contact contact = ContactsDao.getInstance().readContacts(account);
		if (contact != null) {
			mCallArray.get(0).setContact(contact);
		}
		CallRecordsDao.getInstance().insertCallRedords(record);
	}

	public ArrayList<ContactSort> getContactSort() {
		return mContactArray;
	}

	public ArrayList<UserInfo> getLoginRecords() {
		return mUserArray;
	}

	public ArrayList<CallRecords> getCallRecords() {
		return mCallArray;
	}
}
