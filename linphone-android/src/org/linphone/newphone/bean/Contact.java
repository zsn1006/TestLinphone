package org.linphone.newphone.bean;

public class Contact {

	private int id;
	private String name;
	private String account;
	private String sipAddr;
	private String icon;
	private String createTime;
	private String address;
	private String phone;
	private String initial;

	public Contact() {
	}

	public Contact(int id, String name, String account, String sipAddr, String icon,
			String createTime, String address, String phone, String initial) {
		this.id = id;
		this.name = name;
		this.account = account;
		this.sipAddr = sipAddr;
		this.icon = icon;
		this.createTime = createTime;
		this.address = address;
		this.phone = phone;
		this.initial = initial;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSipAddr() {
		return sipAddr;
	}

	public void setSipAddr(String sipAddr) {
		this.sipAddr = sipAddr;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

}
