package org.linphone.newphone.bean;

public class UserInfo {

	private int id;
	private String name;
	private String account;
	private String icon;
	private String password;
	private String ip;
	private String port;
	private String proxy;
	private String initial;

	public UserInfo() {
	}

	public UserInfo(int id, String name, String account, String icon, String password, String ip,
			String port, String proxy, String initial) {
		this.id = id;
		this.name = name;
		this.account = account;
		this.icon = icon;
		this.password = password;
		this.ip = ip;
		this.port = port;
		this.proxy = proxy;
		this.initial = initial;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

}
